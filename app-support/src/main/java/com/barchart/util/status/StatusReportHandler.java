package com.barchart.util.status;

import io.netty.handler.codec.http.HttpResponseStatus;

import java.io.IOException;
import java.util.Set;

import com.barchart.netty.server.http.request.HttpServerRequest;
import com.barchart.netty.server.http.request.RequestHandlerBase;
import com.barchart.util.common.status.GroupStatus;
import com.barchart.util.common.status.MemberStatus;

public class StatusReportHandler extends RequestHandlerBase {

	private final Set<GroupStatus> clusters;

	public StatusReportHandler(final Set<GroupStatus> clusters_) {
		clusters = clusters_;
	}

	@Override
	public void handle(final HttpServerRequest request) throws IOException {

		// Basic system stats
		request.response().setStatus(HttpResponseStatus.OK);
		request.response().setContentType("application/json");
		request.response().write("{\n" //
				+ "\t\"status\": \"OK\"\n" //
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
