package com.barchart.util.status;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.file.FileStore;
import java.nio.file.FileSystemException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.barchart.netty.server.http.request.HttpServerRequest;
import com.barchart.netty.server.http.request.RequestHandlerBase;
import com.barchart.util.common.status.ComponentStatus;
import com.barchart.util.common.status.GroupStatus;
import com.barchart.util.common.status.MemberStatus;
import com.barchart.util.common.status.ScalingMonitor;
import com.barchart.util.common.status.ScalingMonitor.Usage;
import com.barchart.util.common.status.StatusType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.collect.ImmutableMap;

public class FullStatusReportHandler extends RequestHandlerBase {

	private final ScalingMonitor scaling;
	private final Set<ComponentStatus> componentStatus;
	private final Set<GroupStatus> clusters;
	private final ObjectMapper mapper;

	public FullStatusReportHandler(final ScalingMonitor scaling_,
			final Set<ComponentStatus> status_, final Set<GroupStatus> clusters_) {
		scaling = scaling_;
		componentStatus = status_;
		clusters = clusters_;
		mapper = new ObjectMapper();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
	}

	@Override
	public void handle(final HttpServerRequest request) throws IOException {

		final Runtime runtime = Runtime.getRuntime();
		final long uptime = ManagementFactory.getRuntimeMXBean().getUptime();
		final double load = ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage();
		final int processors = ManagementFactory.getOperatingSystemMXBean().getAvailableProcessors();

		final ScalingMonitor.Usage usage = scaling != null ? scaling.usage() : Usage.NORMAL;

		StatusType status = StatusType.OK;
		String message = "";

		// Calculate based on ComponentStatus rules
		if (componentStatus != null) {
			for (final ComponentStatus cs : componentStatus) {
				if (cs.status() != StatusType.OK) {
					switch (cs.optionality()) {
						case IGNORED:
							continue;
						case REQUIRED:
							if (cs.status().ordinal() > status.ordinal()) {
								status = cs.status();
							}
							break;
						case OPTIONAL:
						default:
							// Optional stuff gets dropped back a level
							if (cs.status() == StatusType.ERROR && StatusType.WARNING.ordinal() > status.ordinal()) {
								status = StatusType.WARNING;
							}
					}

					if (!message.isEmpty()) {
						message += "; ";
					}
					message += cs.message();

				}

			}

		}

		final ImmutableMap.Builder<String, Object> builder = new ImmutableMap.Builder<String, Object>()
				.put("resources", usage)
				.put("status", status)
				.put("message", message)
				.put("uptime", getElapsedTimeString(uptime))
				.put("uptime_ms", uptime)
				.put("load", new ImmutableMap.Builder<String, Object>()
						.put("total", load)
						.put("per_cpu", load / processors)
						.build())
				.put("memory", new ImmutableMap.Builder<String, Object>()
						.put("allocated", getByteCountAsString(runtime.totalMemory()))
						.put("allocated_bytes", runtime.totalMemory())
						.put("free", getByteCountAsString(runtime.freeMemory()))
						.put("free_bytes", runtime.freeMemory())
						.put("max", getByteCountAsString(runtime.maxMemory()))
						.put("max_bytes", runtime.maxMemory())
						.build());

		final List<Map<String, Object>> storage = new ArrayList<Map<String, Object>>();

		for (final Path root : FileSystems.getDefault().getRootDirectories()) {
			try {
				final FileStore store = Files.getFileStore(root);
				storage.add(new ImmutableMap.Builder<String, Object>()
						.put("root", root.toString())
						.put("total", getByteCountAsString(store.getTotalSpace()))
						.put("total_bytes", store.getTotalSpace())
						.put("free", getByteCountAsString(store.getUsableSpace()))
						.put("free_bytes", store.getUsableSpace())
						.build());
			} catch (final FileSystemException e) {}
		}

		builder.put("storage", storage);

		if (componentStatus != null) {

			final List<Map<String, Object>> componentList = new ArrayList<Map<String, Object>>();

			for (final ComponentStatus cs : componentStatus) {

				componentList.add(new ImmutableMap.Builder<String, Object>()
						.put("name", cs.name())
						.put("status", cs.status())
						.put("locality", cs.locality())
						.put("optionality", cs.optionality())
						.put("message", cs.message())
						.put("timestamp", cs.timestamp())
						.put("errorCount", cs.errorCount())
						.put("flaps_5min", cs.flaps(5))
						.put("flaps_15min", cs.flaps(15))
						.put("flaps_60min", cs.flaps(60))
						.build());
			}

			builder.put("components", componentList);

		}

		if (clusters != null) {

			final List<Map<String, Object>> clusterList = new ArrayList<Map<String, Object>>();

			for (final GroupStatus gs : clusters) {

				final List<Map<String, Object>> nodeList = new ArrayList<Map<String, Object>>();
				for (final MemberStatus ms : gs.groupMembers()) {
					nodeList.add(new ImmutableMap.Builder<String,Object>()
							.put("id", ms.serviceId())
							.put("address", ms.serviceAddress())
							.put("status", ms.serviceState())
							.put("message", ms.serviceStatus())
							.build());
				}

				clusterList.add(new ImmutableMap.Builder<String, Object>()
						.put("name", gs.groupName())
						.put("type", gs.groupType())
						.put("status", gs.groupState())
						.put("message", gs.groupStatus())
						.put("nodes", nodeList)
						.build());

			}

			builder.put("clusters", clusterList);

		}

		// TODO show installed packages by scanning META-INF/maven/*

		final Map<String, Object> health = builder.build();

		mapper.writeValue(request.response().getOutputStream(), health);
	}

	private String getElapsedTimeString(long elapsed) {

		final long days = TimeUnit.MILLISECONDS.toDays(elapsed);
		elapsed -= TimeUnit.DAYS.toMillis(days);
		final long hours = TimeUnit.MILLISECONDS.toHours(elapsed);
		elapsed -= TimeUnit.HOURS.toMillis(hours);
		final long minutes = TimeUnit.MILLISECONDS.toMinutes(elapsed);
		elapsed -= TimeUnit.MINUTES.toMillis(minutes);
		final long seconds = TimeUnit.MILLISECONDS.toSeconds(elapsed);
		elapsed -= TimeUnit.SECONDS.toMillis(seconds);

		if (days > 0) {
			return days + "d "
					+ String.format("%02d:%02d:%02d", hours, minutes, seconds);
		} else {
			return String.format("%02d:%02d:%02d", hours, minutes, seconds);
		}

	}

	private String getByteCountAsString(final long bytes) {

		final java.text.NumberFormat nf =
				java.text.NumberFormat.getNumberInstance();

		nf.setMaximumFractionDigits(2);

		if (bytes == 1) {
			return "1 byte";
		}

		if (bytes < 1024) {
			return nf.format(bytes) + " bytes";
		}

		double d = (bytes / 1024.0d);
		if (d < 1024) {
			return nf.format(d) + " KB";
		}

		d = (d / 1024.0d);
		if (d < 1024) {
			return nf.format(d) + " MB";
		}

		d = (d / 1024.0d);

		if (d < 1024) {
			return nf.format(d) + " GB";
		}

		d = d / 1024.0;

		return nf.format(d) + " TB";

	}

	private String getServiceGroups() {

		if (clusters != null) {

			final StringBuffer ret = new StringBuffer();

			ret.append(",\n\t\t\"clusters\": \n" //
					+ "\t\t[\n");

			for (final GroupStatus gs : clusters) {
				ret.append("\t\t{\n" //
						+ "\t\t\t\"state\": \"" + gs.groupStatus() + "\",\n" //
						+ "\t\t\t\"size\": " + gs.groupMembers().size() //
						+ getNodes(gs.groupMembers()) //
						+ "\n\t\t}\n");

			}

			ret.append("\t\t]");

			return ret.toString();

		}

		return "";

	}

	private String getNodes(final Set<MemberStatus> mss) {

		final StringBuilder ret = new StringBuilder();
		boolean first = true;
		ret.append(",\n\t\t\t\"nodes\":\n" //
				+ "\t\t\t[\n");
		for (final MemberStatus ms : mss) {
			if (first == true) {
				first = false;
			} else {
				ret.append(",\n");
			}

			ret.append("\t\t\t\t{\n" //
					+ "\t\t\t\t\t\"id\": \"" + ms.serviceId() + "\",\n" //
					+ "\t\t\t\t\t\"address\": \"" + ms.serviceAddress() + "\",\n" //
					+ "\t\t\t\t\t\"state\": \"" + ms.serviceState() + "\",\n" //
					+ "\t\t\t\t\t\"status\": \"" + ms.serviceStatus() + "\"\n" //
					+ "\t\t\t\t}");
		}
		ret.append("\n\t\t\t]");
		return ret.toString();
	}

}
