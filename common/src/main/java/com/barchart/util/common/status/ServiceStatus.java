package com.barchart.util.common.status;

public interface ServiceStatus {

	public String serviceId();

	public String serviceName();

	public ServiceState serviceState();

	public String serviceStatus();

}
