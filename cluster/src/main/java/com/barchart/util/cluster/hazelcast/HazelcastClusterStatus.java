package com.barchart.util.cluster.hazelcast;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.barchart.util.common.status.GroupStatus;
import com.barchart.util.common.status.MemberStatus;
import com.barchart.util.common.status.ServiceState;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.Member;
import com.hazelcast.partition.MigrationEvent;
import com.hazelcast.partition.MigrationListener;

@Component(immediate = true)
public class HazelcastClusterStatus extends DefaultComponent implements
		GroupStatus {

	/*
	 * Cluster health reporting for admin interface
	 */

	private final Collection<MigrationEvent> migrations = new HashSet<MigrationEvent>();
	private int failures = 0;

	protected void addListeners(final HazelcastInstance instance) {

		if (!(instance instanceof HazelcastClient)) {
			instance.getPartitionService().addMigrationListener(
					new MigrationListener() {

						@Override
						public void migrationStarted(
								final MigrationEvent migrationEvent) {
							migrations.add(migrationEvent);
						}

						@Override
						public void migrationCompleted(
								final MigrationEvent migrationEvent) {
							migrations.remove(migrationEvent);
						}

						@Override
						public void migrationFailed(
								final MigrationEvent migrationEvent) {
							migrations.remove(migrationEvent);
							failures++;
						}
					});
		}

	}

	@Override
	public String groupId() {
		if (cluster.getInstance() != null) {
			return cluster.getInstance().getName();
		}
		return null;
	}

	@Override
	public String groupType() {
		return "hazelcast";
	}

	@Override
	public String groupName() {
		if (cluster.getInstance() != null) {
			if (cluster.getInstance() instanceof HazelcastClient) {
				return "client only";
			} else {
				return cluster.getInstance().getConfig().getGroupConfig()
						.getName();
			}
		}
		return null;
	}

	@Override
	public ServiceState groupState() {
		// Don't have a lot of tools from Hazelcast to determine overall cluster
		// health, so we'll just check active partition migrations
		if (migrations.size() > 0) {
			return ServiceState.WORKING;
		}
		return ServiceState.OK;
	}

	@Override
	public String groupStatus() {
		if (cluster.getInstance() != null) {
			int data = 0;
			int clients = 0;
			for (final Member m : cluster.getInstance().getCluster()
					.getMembers()) {
				if (m.isLiteMember()) {
					clients++;
				} else {
					data++;
				}
			}
			final StringBuilder status = new StringBuilder();
			status.append(data + " data nodes, " + clients + " clients, "
					+ failures + " partition migration errors");
			return status.toString();
		}
		return null;
	}

	@Override
	public Set<MemberStatus> groupMembers() {
		final Set<MemberStatus> ms = new HashSet<MemberStatus>();
		if (cluster.getInstance() != null) {
			for (final Member m : cluster.getInstance().getCluster()
					.getMembers()) {
				ms.add(new HazelcastMemberStatus(m));
			}
		}
		return ms;
	}

	private class HazelcastMemberStatus implements MemberStatus {

		private final Member member;

		HazelcastMemberStatus(final Member member_) {
			member = member_;
		}

		@Override
		public String serviceId() {
			if (member.localMember()) {
				return member.getUuid() + " (local node)";
			}
			return member.getUuid();
		}

		@Override
		public String serviceName() {
			return id();
		}

		@Override
		public String serviceAddress() {
			return member.getInetSocketAddress().toString();
		}

		@Override
		public ServiceState serviceState() {
			return ServiceState.OK;
		}

		@Override
		public String serviceStatus() {
			return member.isLiteMember() ? "Client only" : "Data node";
		}

	}

	HazelcastCluster cluster;

	@Reference
	protected void bind(final HazelcastCluster cluster_) {
		cluster = cluster_;
		addListeners(cluster.getInstance());
	}

	protected void unbind(final HazelcastCluster cluster_) {
		cluster = null;
	}

}
