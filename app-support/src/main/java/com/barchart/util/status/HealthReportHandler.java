package com.barchart.util.status;

import io.netty.handler.codec.http.HttpResponseStatus;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.concurrent.TimeUnit;

import com.barchart.netty.server.http.request.HttpServerRequest;
import com.barchart.netty.server.http.request.RequestHandlerBase;

public class HealthReportHandler extends RequestHandlerBase {

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
				+ "\n}");
		request.response().finish();

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
