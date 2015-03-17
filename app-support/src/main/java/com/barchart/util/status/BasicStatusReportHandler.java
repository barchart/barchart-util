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
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.barchart.netty.server.http.request.HttpServerRequest;
import com.barchart.netty.server.http.request.RequestHandlerBase;
import com.barchart.util.common.status.ComponentStatus;
import com.barchart.util.common.status.ScalingMonitor;
import com.barchart.util.common.status.ScalingMonitor.Usage;
import com.barchart.util.common.status.StatusType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;

public class BasicStatusReportHandler extends RequestHandlerBase {

	private static Logger log = LoggerFactory.getLogger(BasicStatusReportHandler.class);

	private final ApplicationStatus appStatus;
	private final ScalingMonitor scaling;
	private final Set<ComponentStatus> componentStatus;
	private final ObjectMapper mapper;

	public BasicStatusReportHandler(final ApplicationStatus appStatus_, final ScalingMonitor scaling_,
			final Set<ComponentStatus> status_) {
		appStatus = appStatus_;
		scaling = scaling_;
		componentStatus = status_;
		mapper = new ObjectMapper();
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
				.put("uptime_ms", uptime)
				.put("load", new ImmutableMap.Builder<String, Object>()
						.put("total", load)
						.put("per_cpu", load / processors)
						.build())
				.put("memory", new ImmutableMap.Builder<String, Object>()
						.put("allocated_bytes", runtime.totalMemory())
						.put("free_bytes", runtime.freeMemory())
						.put("max_bytes", runtime.maxMemory())
						.build());

		final List<Map<String, Object>> storage = new ArrayList<Map<String, Object>>();

		for (final Path root : FileSystems.getDefault().getRootDirectories()) {
			try {
				final FileStore store = Files.getFileStore(root);
				storage.add(new ImmutableMap.Builder<String, Object>()
						.put("root", root.toString())
						.put("total_bytes", store.getTotalSpace())
						.put("free_bytes", store.getUsableSpace())
						.build());
			} catch (final FileSystemException e) {}
		}

		builder.put("storage", storage);

		final Map<String, Object> health = builder.build();

		if (appStatus.status() == StatusType.ERROR) {
			request.response().setStatus(HttpResponseStatus.INTERNAL_SERVER_ERROR);
		} else {
			request.response().setStatus(HttpResponseStatus.OK);
		}
		request.response().setContentType("application/json");
		mapper.writeValue(request.response().getOutputStream(), health);

		// setMDC(health, "");
		// log.info(message);
		// MDC.clear();

	}

	private void setMDC(final Map<String,Object> map, final String prefix) {
		for (final Map.Entry<String, Object> entry : map.entrySet()) {
			if (entry.getValue() instanceof Map) {
				setMDC((Map<String, Object>) entry.getValue(), entry.getKey() + "_");
			} else if (entry.getValue() instanceof List) {
				int idx = 0;
				for (final Object e : (List<Object>) entry.getValue()) {
					if (e instanceof Map) {
						setMDC((Map<String, Object>) e, entry.getKey() + "_" + idx + "_");
					} else {
						MDC.put(prefix + idx + "_" + entry.getKey(), entry.getValue().toString());
					}
					idx++;
				}
			} else {
				MDC.put(prefix + entry.getKey(), entry.getValue().toString());
			}
		}
	}

}
