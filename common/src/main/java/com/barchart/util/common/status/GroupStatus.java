package com.barchart.util.common.status;

import java.util.Set;

/**
 * Reports on the current status of a application service cluster (hazelcast,
 * elasticsearch, etc).
 *
 * @author jeremy
 *
 */

public interface GroupStatus {

	public String groupId();

	public String groupName();

	public String groupType();

	public ServiceState groupState();

	public String groupStatus();

	public Set<MemberStatus> groupMembers();

}