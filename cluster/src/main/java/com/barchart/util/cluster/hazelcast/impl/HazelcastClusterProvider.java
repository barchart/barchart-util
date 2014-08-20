package com.barchart.util.cluster.hazelcast.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;

import com.barchart.news.server.api.SharedExecutorService;
import com.barchart.osgi.component.base.BaseComponent;
import com.barchart.util.cluster.hazelcast.HazelcastCluster;
import com.barchart.util.cluster.hazelcast.HazelcastStoreRegistry;
import com.barchart.util.common.aws.EC2Util;
import com.hazelcast.client.ClientConfig;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.config.AwsConfig;
import com.hazelcast.config.Config;
import com.hazelcast.config.Join;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MapStoreConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.MapLoader;

@Component(name = HazelcastClusterProvider.NAME, immediate = true, configurationPolicy = ConfigurationPolicy.REQUIRE)
public class HazelcastClusterProvider extends BaseComponent implements
		HazelcastCluster {

	public static final String NAME = "barchart.news.modules.hazelcast.cluster";

	private HazelcastInstance hazelcast;

	private volatile CountDownLatch updateLatch;

	@Override
	protected void activate() throws Exception {
		super.activate();
		configure();
	}

	@Override
	protected void modified() throws Exception {
		super.modified();
		configure();
	}

	@Override
	protected void deactivate() throws Exception {

		super.deactivate();

		// Use getter to block and avoid shutdown during initialization
		final HazelcastInstance active = getInstance();

		if (active != null) {
			active.getLifecycleService().shutdown();
			hazelcast = null;
		}

	}

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

		if (configCurrent().hasPath("client")
				&& configCurrent().getBoolean("client")) {
			configureClient();
		} else {
			configureNode();
		}

	}

	private void configureClient() {

		final ClientConfig cfg = new ClientConfig();

		// Group config
		if (configCurrent().hasPath("cluster-name")) {
			cfg.getGroupConfig().setName(
					configCurrent().getString("cluster-name"));
		}

		// Network config
		if (configCurrent().hasPath("members")) {
			for (final String member : configCurrent().getStringList("members")) {
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

		final Config cfg = new Config();

		// Group config
		if (configCurrent().hasPath("cluster-name")) {
			cfg.getGroupConfig().setName(
					configCurrent().getString("cluster-name"));
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

		if (configCurrent().hasPath("port")) {
			network.setPort(configCurrent().getInt("port"));
		}

		final Join join = network.getJoin();

		if (configCurrent().hasPath("aws-access-key")) {

			join.getMulticastConfig().setEnabled(false);

			final AwsConfig aws = join.getAwsConfig();

			aws.setEnabled(true);

			aws.setAccessKey(configCurrent().getString("aws-access-key"));
			aws.setSecretKey(configCurrent().getString("aws-secret-key"));

			if (configCurrent().hasPath("aws-region")) {
				aws.setRegion(configCurrent().getString("aws-region"));
			}

			if (configCurrent().hasPath("aws-security-group")) {
				aws.setSecurityGroupName(configCurrent().getString(
						"aws-security-group"));
			} else {

				// Try EC2 userdata lookup
				final com.typesafe.config.Config userData = EC2Util
						.getUserData();

				if (userData != null
						&& userData.root().containsKey(
								"barchart.config.hazelcast.group")) {
					final String secGroup = (String) userData.root()
							.get("barchart.config.hazelcast.group").unwrapped();
					log.debug("Using user-data provided security group "
							+ secGroup);
					aws.setSecurityGroupName(secGroup);
				}

			}

			if (configCurrent().hasPath("aws-tag-key")) {
				aws.setTagKey(configCurrent().getString("aws-tag-key"));
				aws.setTagValue(configCurrent().getString("aws-tag-value"));
			}

		} else {
			join.getAwsConfig().setEnabled(false);
		}

		if (configCurrent().hasPath("multicast")) {

			join.getMulticastConfig().setEnabled(
					configCurrent().getBoolean("multicast"));

			if (configCurrent().hasPath("multicast-group")) {

				join.getMulticastConfig().setMulticastGroup(
						configCurrent().getString("multicast-group"));

				if (configCurrent().hasPath("multicast-port")) {
					join.getMulticastConfig().setMulticastPort(
							configCurrent().getInt("multicast-port"));
				}

			}

		}

		if (configCurrent().hasPath("members")) {

			join.getTcpIpConfig().setEnabled(true);

			for (final String member : configCurrent().getStringList("members")) {
				join.getTcpIpConfig().addMember(member);
			}

		}

		if (configCurrent().hasPath("interface")) {
			network.getInterfaces().setEnabled(true)
					.addInterface(configCurrent().getString("interface"));
		}

		return network;

	}

	private Map<String, MapConfig> getMapConfigs() {

		final Map<String, MapConfig> configs = new HashMap<String, MapConfig>();

		if (configCurrent().hasPath("maps")) {

			for (final com.typesafe.config.Config map : configCurrent()
					.getConfigList("maps")) {

				final MapConfig mapConfig = new MapConfig();

				mapConfig.setName(map.getString("name"));
				mapConfig.setBackupCount(getConfigInt("backup-count", 2));
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

	HazelcastStoreRegistry storeRegistry;

	@Reference
	protected void bind(final HazelcastStoreRegistry sr_) {
		storeRegistry = sr_;
	}

	protected void unbind(final HazelcastStoreRegistry sr_) {
		storeRegistry = null;
	}

	SharedExecutorService executor;

	@Reference
	protected void bind(final SharedExecutorService ex) {
		executor = ex;
	}

	protected void unbind(final SharedExecutorService ex) {
		executor = null;
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
