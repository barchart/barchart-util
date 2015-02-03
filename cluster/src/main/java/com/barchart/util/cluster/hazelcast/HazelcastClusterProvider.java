package com.barchart.util.cluster.hazelcast;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.util.common.aws.EC2Util;
import com.barchart.util.guice.Activate;
import com.barchart.util.guice.Component;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.hazelcast.client.ClientConfig;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.config.AwsConfig;
import com.hazelcast.config.Join;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MapStoreConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.MapLoader;
import com.typesafe.config.Config;

@Component(HazelcastClusterProvider.TYPE)
public class HazelcastClusterProvider implements HazelcastCluster {

	public static final String TYPE = "barchart.util.cluster.hazelcast";

	private static final Logger log = LoggerFactory.getLogger(HazelcastClusterProvider.class);

	private HazelcastInstance hazelcast;

	private volatile CountDownLatch updateLatch;

	@Inject
	HazelcastStoreRegistry storeRegistry;

	@Inject
	@Named("#")
	Config config;

	@Activate
	protected void configure() {

		// Block on existing initialization to avoid conflicts
		final CountDownLatch existing = updateLatch;

		if (existing != null) {
			try {
				existing.await();
			} catch (final InterruptedException e) {
			}
		}

		updateLatch = new CountDownLatch(1);

		if (config.hasPath("client")
				&& config.getBoolean("client")) {
			configureClient();
		} else {
			configureNode();
		}

	}

	private void configureClient() {

		final ClientConfig cfg = new ClientConfig();

		// Group config
		if (config.hasPath("cluster-name")) {
			cfg.getGroupConfig().setName(config.getString("cluster-name"));
		}

		// Network config
		if (config.hasPath("members")) {
			for (final String member : config.getStringList("members")) {
				cfg.addAddress(member);
			}
		}

		// Start asynchronously to allow other components to startup
		// (getInstance() will block until ready)
		configure(new Callable<HazelcastInstance>() {
			@Override
			public HazelcastInstance call() throws Exception {
				return HazelcastClient.newHazelcastClient(cfg);
			}
		});

	}

	private void configureNode() {

		final com.hazelcast.config.Config cfg = new com.hazelcast.config.Config();

		// Group config
		if (config.hasPath("cluster-name")) {
			cfg.getGroupConfig().setName(
					config.getString("cluster-name"));
		}

		// Map config
		cfg.setMapConfigs(getMapConfigs());

		// Network config
		cfg.setNetworkConfig(getNetworkConfig());

		// Initialize instance
		configure(new Callable<HazelcastInstance>() {
			@Override
			public HazelcastInstance call() throws Exception {
				return Hazelcast.newHazelcastInstance(cfg);
			}
		});

	}

	private void configure(final Callable<HazelcastInstance> initializer) {

		try {

			final HazelcastInstance existing = hazelcast;
			hazelcast = null;

			// Startup instance, wait for it to be available
			if (existing != null) {
				existing.getLifecycleService().shutdown();
			}

			hazelcast = initializer.call();

		} catch (final Exception e) {

			log.warn("Could not initialize node");

		} finally {

			if (updateLatch != null) {
				final CountDownLatch l = updateLatch;
				// Null out before countDown to avoid sync issue with
				// getInstance()
				updateLatch = null;
				l.countDown();
			}

		}

	}

	private NetworkConfig getNetworkConfig() {

		// Network config

		final NetworkConfig network = new NetworkConfig();

		if (config.hasPath("port")) {
			network.setPort(config.getInt("port"));
		}

		final Join join = network.getJoin();

		if (config.hasPath("aws-access-key")) {

			join.getMulticastConfig().setEnabled(false);

			final AwsConfig aws = join.getAwsConfig();

			aws.setEnabled(true);

			aws.setAccessKey(config.getString("aws-access-key"));
			aws.setSecretKey(config.getString("aws-secret-key"));

			if (config.hasPath("aws-region")) {
				aws.setRegion(config.getString("aws-region"));
			}

			String secGroup = null;
			if (config.hasPath("aws-security-group")) {
				secGroup = config.getString("aws-security-group");
			} else {

				// Try EC2 userdata lookup
				final com.typesafe.config.Config userData = EC2Util.getUserData();

				if (userData != null && userData.root().containsKey("barchart.config.hazelcast.group")) {
					secGroup = (String) userData.root().get("barchart.config.hazelcast.group").unwrapped();
					log.debug("Using user-data provided security group " + secGroup);
				}

			}

			if (secGroup != null) {

				log.debug("Using provided security group '" + secGroup + "'");

				if (secGroup.contains("*")) {
					final String pattern = "^" + secGroup.replace("*", ".*") + "$";
					log.trace("Match pattern: " + pattern);
					final Pattern p = Pattern.compile(pattern);
					for (final String sg : EC2Util.getSecurityGroups()) {
						log.trace("Checking instance security group '" + sg + "'");
						if (p.matcher(sg).matches()) {
							log.trace("Matched security group '" + sg + "'");
							secGroup = sg;
							break;
						}
					}
				}

				log.debug("Attempting node discovery with security group '" + secGroup + "'");

				aws.setSecurityGroupName(secGroup);

			}

			if (config.hasPath("aws-tag-key")) {
				aws.setTagKey(config.getString("aws-tag-key"));
				aws.setTagValue(config.getString("aws-tag-value"));
			}

		} else {
			join.getAwsConfig().setEnabled(false);
		}

		if (config.hasPath("multicast")) {

			join.getMulticastConfig().setEnabled(
					config.getBoolean("multicast"));

			if (config.hasPath("multicast-group")) {

				join.getMulticastConfig().setMulticastGroup(
						config.getString("multicast-group"));

				if (config.hasPath("multicast-port")) {
					join.getMulticastConfig().setMulticastPort(
							config.getInt("multicast-port"));
				}

			}

		}

		if (config.hasPath("members")) {

			join.getTcpIpConfig().setEnabled(true);

			for (final String member : config.getStringList("members")) {
				join.getTcpIpConfig().addMember(member);
			}

		}

		if (config.hasPath("interface")) {
			network.getInterfaces().setEnabled(true)
					.addInterface(config.getString("interface"));
		}

		return network;

	}

	private Map<String, MapConfig> getMapConfigs() {

		final Map<String, MapConfig> configs = new HashMap<String, MapConfig>();

		if (config.hasPath("maps")) {

			for (final com.typesafe.config.Config map : config
					.getConfigList("maps")) {

				final MapConfig mapConfig = new MapConfig();

				mapConfig.setName(map.getString("name"));
				if (map.hasPath("backup-count")) {
					mapConfig.setBackupCount(map.getInt("backup-count"));
				} else {
					mapConfig.setBackupCount(2);
				}

				if (map.hasPath("size")) {
					mapConfig.getMaxSizeConfig().setSize(map.getInt("size"));
				}
				if (map.hasPath("expiration")) {
					mapConfig.setMaxIdleSeconds(map.getInt("expiration"));
				}
				if (map.hasPath("eviction")) {
					mapConfig.setEvictionPolicy(map.getString("eviction"));
				}

				if (map.hasPath("store")) {

					final MapLoader<String, ?> adapter = storeRegistry
							.getStore(map.getString("store"));

					if (adapter != null) {

						final MapStoreConfig mapStoreCfg = new MapStoreConfig();

						mapStoreCfg.setImplementation(adapter);
						mapStoreCfg.setEnabled(true);

						if (map.hasPath("write-delay")) {
							mapStoreCfg.setWriteDelaySeconds(map
									.getInt("write-delay"));
						}

						mapConfig.setMapStoreConfig(mapStoreCfg);

					} else {

						log.warn("Missing store adapter "
								+ map.getString("store"));

					}

				}

				configs.put(mapConfig.getName(), mapConfig);

			}

		}

		return configs;

	}

	@Override
	public HazelcastInstance getInstance() {
		final CountDownLatch latch = updateLatch;
		if (latch != null) {
			try {
				latch.await();
			} catch (final InterruptedException e) {
			}
		}
		return hazelcast;
	}

	@Override
	public HazelcastInstance getInstance(final long timeout, final TimeUnit unit)
			throws InterruptedException {
		final CountDownLatch latch = updateLatch;
		if (latch != null) {
			latch.await(timeout, unit);
		}
		return hazelcast;
	}

}
