package com.barchart.util.status;

import java.util.Set;

import com.barchart.util.common.status.BaseStatus;
import com.barchart.util.common.status.ComponentStatus;
import com.barchart.util.common.status.StatusType;
import com.typesafe.config.Config;

public class ApplicationStatus extends BaseStatus {

	private final Set<ComponentStatus> componentStatus;
	private final Config overrides;

	public ApplicationStatus(final Set<ComponentStatus> status_, final Config overrides_) {
		super("application");
		componentStatus = status_;
		overrides = overrides_;
	}

	public void update() {

		StatusType status = StatusType.OK;
		String message = "";

		// Calculate based on ComponentStatus rules
		if (componentStatus != null) {

			for (final ComponentStatus cs : componentStatus) {

				if (cs.status() != StatusType.OK) {

					ComponentStatus.Optionality opt = cs.optionality();
					if (overrides != null && overrides.hasPath(cs.name())) {
						opt = ComponentStatus.Optionality.valueOf(overrides.getString(cs.name()));
					}

					switch (opt) {
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

		status(status, message);

	}

}
