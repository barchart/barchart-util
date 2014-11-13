package com.barchart.util.health;

import io.netty.handler.codec.http.HttpResponseStatus;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.barchart.netty.guice.http.AbstractHttpRequestHandler;
import com.barchart.netty.server.http.request.HttpServerRequest;
import com.barchart.util.common.status.GroupStatus;
import com.barchart.util.common.status.MemberStatus;
import com.barchart.util.guice.Component;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.typesafe.config.Config;

@Component("com.barchart.util.health.rest")
public class HealthReportHandler extends AbstractHttpRequestHandler {

	@Inject
	@Named("#")
	private Config config;

	@Inject(optional = true)
	private Set<GroupStatus> clusters;

	@Override
	public String path() {

		if (config.hasPath("path"))
			return config.getString("path");

		return "/health";

	}

	@Override
	public void handle(final HttpServerRequest request) throws IOException {

		final Runtime runtime = Runtime.getRuntime();
		final long uptime = ManagementFactory.getRuntimeMXBean().getUptime();
		final double load =
				ManagementFactory.getOperatingSystemMXBean()
						.getSystemLoadAverage();

		// Basic system stats
		request.response().setStatus(HttpResponseStatus.OK);
		request.response().setContentType("application/json");
		request.response().write("{\n" //
				+ "\t\"status\": \"OK\",\n" //
				+ "\t\"uptime\": \"" + getElapsedTimeString(uptime) + "\",\n" //
				+ "\t\"load\": " + String.format("%.2f", load) + ",\n" //
				+ "\t\"memory\": \n" //
				+ "\t{\n" //
				+ "\t\t\"allocated\": " + runtime.totalMemory() + ",\n" //
				+ "\t\t\"free\": " + runtime.freeMemory() + ",\n" //
				+ "\t\t\"max\": " + runtime.maxMemory() + "\n" //
				+ "\t}" //
				+ getServiceGroups() //
				+ "\n}");
		request.response().finish();

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
		ret.append(",\n\t\t\t\"nodes\":\n" //
				+ "\t\t\t[\n");
		for (final MemberStatus ms : mss) {
			ret.append("\t\t\t\t{\n" //
					+ "\t\t\t\t\t\"id\": \"" + ms.serviceId() + "\",\n" //
					+ "\t\t\t\t\t\"address\": \"" + ms.serviceAddress() + "\",\n" //
					+ "\t\t\t\t\t\"state\": \"" + ms.serviceState() + "\",\n" //
					+ "\t\t\t\t\t\"status\": \"" + ms.serviceStatus() + "\"\n" //
					+ "\t\t\t\t}\n");
		}
		ret.append("\t\t\t]");
		return ret.toString();
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
