package com.barchart.util.status;

import java.util.Set;

import com.barchart.netty.guice.http.HttpRequestHandler;
import com.barchart.netty.rest.server.RestServiceBase;
import com.barchart.util.common.status.GroupStatus;
import com.barchart.util.guice.Component;
import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * Root module for REST services. Uses a Router internally for request processing. Subclass to create independent
 * injectable service modules that have multiple internal REST service endpoints.
 */
@Component(StatusRestService.TYPE)
public class StatusRestService extends RestServiceBase implements HttpRequestHandler {

	public static final String TYPE = "com.barchart.util.status.rest";

	@Inject(optional = true)
	@Named("#path")
	private String path = "/status";

	@Inject
	private Set<GroupStatus> clusters;

	public StatusRestService() {
		add("/", new StatusReportHandler(clusters));
		add("/health", new HealthReportHandler());
	}

	@Override
	public String path() {
		return path;
	}

}
