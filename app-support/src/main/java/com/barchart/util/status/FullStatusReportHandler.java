package com.barchart.util.status;

import io.netty.handler.codec.http.HttpResponseStatus;

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
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.barchart.netty.server.http.request.HttpServerRequest;
import com.barchart.netty.server.http.request.RequestHandlerBase;
import com.barchart.util.common.status.ComponentStatus;
import com.barchart.util.common.status.NodeStatus;
import com.barchart.util.common.status.ScalingMonitor;
import com.barchart.util.common.status.ScalingMonitor.Usage;
import com.barchart.util.common.status.Statistics;
import com.barchart.util.common.status.StatusType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.collect.ImmutableMap;
import com.google.common.reflect.ClassPath;

public class FullStatusReportHandler extends RequestHandlerBase {

	private final ApplicationStatus appStatus;
	private final ScalingMonitor scaling;
	private final Set<ComponentStatus> componentStatus;
	private final ObjectMapper mapper;

	public FullStatusReportHandler(final ApplicationStatus appStatus_, final ScalingMonitor scaling_,
			final Set<ComponentStatus> status_) {
		appStatus = appStatus_;
		scaling = scaling_;
		componentStatus = status_;
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

		appStatus.update();

		final ImmutableMap.Builder<String, Object> builder = new ImmutableMap.Builder<String, Object>()
				.put("resources", usage)
				.put("status", appStatus.status())
				.put("message", appStatus.message())
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

				final ImmutableMap.Builder<String, Object> componentBuilder =
						new ImmutableMap.Builder<String, Object>()
						.put("name", cs.name())
						.put("status", cs.status())
						.put("locality", cs.locality())
						.put("optionality", cs.optionality())
						.put("message", cs.message())
						.put("timestamp", cs.timestamp())
						.put("errorCount", cs.errorCount())
						.put("flaps_5min", cs.flaps(5))
						.put("flaps_15min", cs.flaps(15))
								.put("flaps_60min", cs.flaps(60));

				if (cs.nodes() != null && cs.nodes().size() > 0) {

					final List<Map<String, Object>> nodeList = new ArrayList<Map<String, Object>>();

					for (final NodeStatus ns : cs.nodes()) {
						nodeList.add(new ImmutableMap.Builder<String, Object>()
								.put("id", ns.id())
								.put("name", ns.name())
								.put("status", ns.status())
								.put("address", ns.address())
								.put("message", ns.message())
								.put("timestamp", ns.timestamp())
								.put("errorCount", ns.errorCount())
								.build());
					}

					componentBuilder.put("nodes", nodeList);

				}

				if (cs.stats() != null && cs.stats().size() > 0) {

					final List<Map<String, Object>> statList = new ArrayList<Map<String, Object>>();

					for (final Statistics st : cs.stats()) {
						ImmutableMap.Builder<String, Object> bld = new ImmutableMap.Builder<String, Object>()
								.put("name", st.name())
								.put("count", st.count());
						if (st.lite() == false) {
							bld.put("min", st.min())
							.put("max", st.max())
									.put("mean", st.mean());
						}
						statList.add(bld.build());
					}

					componentBuilder.put("statistics", statList);

				}

				componentList.add(componentBuilder.build());

			}

			builder.put("components", componentList);

		}

		// Show installed module version
		final List<Map<String, Object>> moduleList = new ArrayList<Map<String, Object>>();

		for (final ClassPath.ResourceInfo r : ClassPath.from(this.getClass().getClassLoader()).getResources()) {

			final String rn = r.getResourceName();
			if (rn.startsWith("META-INF/maven")) {
				System.out.println(rn);
			}
			if (rn.startsWith("META-INF/maven") && rn.endsWith("pom.properties")) {
				final Properties p = new Properties();
				p.load(getClass().getClassLoader().getResourceAsStream(rn));
				moduleList.add(new ImmutableMap.Builder<String, Object>()
						.put("module", p.getProperty("groupId") + "/" + p.getProperty("artifactId"))
						.put("version", p.getProperty("version"))
						.build());
			}

		}

		if (moduleList.size() > 0) {
			builder.put("modules", moduleList);
		}

		final Map<String, Object> health = builder.build();

		if (appStatus.status() == StatusType.ERROR) {
			request.response().setStatus(HttpResponseStatus.INTERNAL_SERVER_ERROR);
		} else {
			request.response().setStatus(HttpResponseStatus.OK);
		}
		request.response().setContentType("application/json");
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

}
