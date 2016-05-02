package com.barchart.util.cluster.hazelcast;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.barchart.util.common.status.BaseComponentStatus;
import com.barchart.util.common.status.BaseNodeStatus;
import com.barchart.util.common.status.NodeStatus;
import com.barchart.util.common.status.StatusType;
import com.barchart.util.guice.Component;
import com.google.inject.Inject;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.config.Config;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.Member;
import com.hazelcast.partition.MigrationEvent;
import com.hazelcast.partition.MigrationListener;

@Component(HazelcastClusterStatus.TYPE)
public class HazelcastClusterStatus extends BaseComponentStatus {

	public static final String TYPE = "com.barchart.util.cluster.hazelcaststatus";

	/*
	 * Cluster health reporting for admin interface
	 */

	private final Collection<MigrationEvent> migrations = new HashSet<MigrationEvent>();
	private final Set<Member> failures = new HashSet<Member>();

	public HazelcastClusterStatus() {
		super("hazelcast", Optionality.REQUIRED, Locality.LOCAL);
	}

	protected void addListeners(final HazelcastInstance instance) {

		if (!(instance instanceof HazelcastClient)) {
			instance.getPartitionService().addMigrationListener(new MigrationListener() {

				@Override
				public void migrationStarted(final MigrationEvent migrationEvent) {
					migrations.add(migrationEvent);
				}

				@Override
				public void migrationCompleted(final MigrationEvent migrationEvent) {
					migrations.remove(migrationEvent);
					failures.remove(migrationEvent.getNewOwner());
					status(StatusType.OK);
				}

				@Override
				public void migrationFailed(final MigrationEvent migrationEvent) {
					migrations.remove(migrationEvent);
					failures.add(migrationEvent.getNewOwner());
					status(StatusType.WARNING);
				}

			});
		}

	}

	@Override
	public String name() {
		try {
			if (cluster.getInstance() != null) {
				HazelcastInstance instance = cluster.getInstance();
				if (instance.getConfig() != null) {
					Config config = instance.getConfig();
					if (config.getGroupConfig() != null) {
						return "hazelcast/" + config.getGroupConfig().getName();
					}
				}
			}
		} catch (Exception ignore) {
		}
		return "hazelcast";
	}

	@Override
	public String message() {
		if (cluster.getInstance() != null) {
			if (migrations.size() > 0) {
				return "Migrating " + migrations.size() + " partitions, " + failures.size() + " migration errors";
			}
			return failures.size() + " migration errors";
		}
		return "";
	}

	@Override
	public Set<NodeStatus> nodes() {
		final Set<NodeStatus> ms = new HashSet<NodeStatus>();
		if (cluster.getInstance() != null) {
			for (final Member m : cluster.getInstance().getCluster().getMembers()) {
				ms.add(new HazelcastNodeStatus(m));
			}
		}
		return ms;
	}

	private class HazelcastNodeStatus extends BaseNodeStatus {

		private final Member member;

		HazelcastNodeStatus(final Member member_) {
			super(member_.getUuid(), member_.isLiteMember() ? "client" : "node",
					member_.getInetSocketAddress().toString());
			member = member_;
		}

		@Override
		public StatusType status() {
			if (failures.contains(member)) {
				return StatusType.WARNING;
			}
			return StatusType.OK;
		}

		@Override
		public String message() {
			if (failures.contains(member)) {
				return "Last migration failed";
			}
			return "";
		}

	}

	HazelcastCluster cluster;

	@Inject
	protected void bind(final HazelcastCluster cluster_) {
		cluster = cluster_;
		addListeners(cluster.getInstance());
	}

}
