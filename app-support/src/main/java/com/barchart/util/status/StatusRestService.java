package com.barchart.util.status;

import java.util.Set;

import com.barchart.netty.guice.http.HttpRequestHandler;
import com.barchart.netty.rest.server.RestServiceBase;
import com.barchart.util.common.status.ComponentStatus;
import com.barchart.util.common.status.ScalingMonitor;
import com.barchart.util.guice.Activate;
import com.barchart.util.guice.Component;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.typesafe.config.Config;

/**
 * Root module for REST services. Uses a Router internally for request processing. Subclass to create independent
 * injectable service modules that have multiple internal REST service endpoints.
 */
@Component(StatusRestService.TYPE)
public class StatusRestService extends RestServiceBase implements HttpRequestHandler {

	public static final String TYPE = "com.barchart.util.status.rest";

	@Inject(optional = true)
	@Named("#")
	private Config config;

	@Inject(optional = true)
	@Named("#path")
	private String path = "/status";

	@Inject(optional = true)
	private Set<ComponentStatus> status;

	@Inject(optional = true)
	private ScalingMonitor scaling;

	@Activate
	public void activate() {

		Config overrides = null;

		if (config.hasPath("optionality")) {
			overrides = config.getConfig("optionality");
		}

		final ApplicationStatus appStatus = new ApplicationStatus(status, overrides);

		final BasicStatusReportHandler basic = new BasicStatusReportHandler(appStatus, scaling, status);

		add("/", basic);
		add("/basic", basic);
		add("/full", new FullStatusReportHandler(appStatus, scaling, status));

	}

	@Override
	public String path() {
		return path;
	}

}
