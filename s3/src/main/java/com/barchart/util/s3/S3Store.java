package com.barchart.util.s3;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rx.Observable;
import rx.functions.Action1;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.Region;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class S3Store {

	protected final Logger log = LoggerFactory.getLogger(getClass());

	private static S3Store INSTANCE;

	protected AmazonS3Client s3;

	protected S3Config config;

	public static class S3Config {

		private final String awsKey;
		private final String awsSecret;

		protected String bucket;
		protected Region region;
		private final String localDir;
		private final String remoteDir;

		public S3Config(String awsKey_, String awsSecret_, String bucket_, String region_, String localDir_,
				String remoteDir_) {
			awsKey = awsKey_;
			awsSecret = awsSecret_;
			bucket = bucket_;
			localDir = localDir_;
			remoteDir = remoteDir_;
			if (region_ != null && !(region_.trim().equals(""))) {
				region = Region.fromValue(region_);
			} else {
				region = Region.US_Standard;
			}

		}

		public String awsKey() {
			return awsKey;
		}

		public String awsSecret() {
			return awsSecret;
		}

		public String bucket() {
			return bucket;
		}

		public Region region() {
			return region;
		}

		public String localDir() {
			return localDir;
		}
	}

	public static S3Store INSTANCE(S3Config config_) {
		if (INSTANCE == null) {
			INSTANCE = new S3Store(config_);
		}
		return INSTANCE;
	}

	private S3Store(S3Config config_) {

		if (config_ != null && config_.bucket() != null) {
			config = config_;

			if (config.awsKey() != null && !(config.awsKey().trim().equals(""))) {
				log.debug("start S3Client with specific secret");
				s3 = new AmazonS3Client(new BasicAWSCredentials(config.awsKey(), config.awsSecret()));
			} else {
				log.debug("start default S3Client");
				s3 = new AmazonS3Client();
			}

			try {
				if (!s3.doesBucketExist(config.bucket())) {
					log.error("S3 bucket " + config.bucket() + " does not exist.");
				}

				final File file = new File(config.localDir());
				if (!file.exists()) {
					if (!file.mkdir()) {
						log.error("failed to create local cache dirctory.");
					}
				}
			} catch (final Exception e) {
				log.error("S3 bucket " + config.bucket() + " does not exist.");
			}
		} else {
			log.error("invalid s3 config. failed to create s3 client");
		}

	}

	public Set<String> lsFiles() {

		final TreeSet<String> files = new TreeSet<String>();
		ObjectListing objects =
				s3.listObjects(new ListObjectsRequest().withBucketName(config.bucket).withPrefix(config.remoteDir));

		// get the file list from S3 Bucket
		do {
			for (final S3ObjectSummary objSummary : objects.getObjectSummaries()) {
				files.add(objSummary.getKey());
			}

			if (objects.isTruncated()) {
				objects = s3.listNextBatchOfObjects(objects);
			} else {
				break;
			}
		} while (true);

		return files;

	}

	public void downloadFiles() {

		final TreeSet<String> files = new TreeSet<String>();
		ObjectListing objects =
				s3.listObjects(new ListObjectsRequest().withBucketName(config.bucket).withPrefix(config.remoteDir));

		// get the file list from S3 Bucket
		do {
			for (final S3ObjectSummary objSummary : objects.getObjectSummaries()) {
				files.add(objSummary.getKey());
			}
			objects = s3.listNextBatchOfObjects(objects);
		} while (objects.isTruncated());

		try {
			FileUtils.deleteDirectory(new File(config.localDir()));
		} catch (final IOException e) {
			e.printStackTrace();
		} finally {
			final File file = new File(config.localDir());
			if (!file.exists()) {
				if (file.mkdir()) {
					for (final String f : files) {
						final String[] fn = f.split("/");
						if (fn.length >= 2) {
							s3.getObject(new GetObjectRequest(config.bucket, f), new File(config.localDir() + "/"
									+ fn[1]));
						}
					}
					log.info("Downloaded " + file.list().length + " files from " + config.bucket);
				} else {
					log.error("failed to create " + config.localDir());
				}
			}
		}

	}

	public void downloadFile(String... filenames) {

		try {
			final TreeSet<String> files = new TreeSet<String>();
			log.info("getting s3 objectlisting");
			ObjectListing objects =
					s3.listObjects(new ListObjectsRequest().withBucketName(config.bucket).withPrefix(config.remoteDir));

			final TreeSet<String> filesToRetrieve = new TreeSet<String>(Arrays.asList(filenames));

			log.info("getting s3 objects");
			// get the file list from S3 Bucket
			do {
				for (final S3ObjectSummary objSummary : objects.getObjectSummaries()) {
					files.add(objSummary.getKey());
				}

				if (objects.isTruncated()) {
					objects = s3.listNextBatchOfObjects(objects);
				} else {
					break;
				}
			} while (true);

			log.info("number of files in S3 is " + files.size());

			final File file = new File(config.localDir());
			if (!file.exists()) {
				if (file.mkdir() != true) {
					log.error("failed to create " + config.localDir());
				} else {
					log.info("created local dir " + config.localDir);
				}
			} else {
				log.info("local dir exists: " + config.localDir);
			}

			for (final String f : filesToRetrieve) {
				if (files.contains(f)) {
					s3.getObject(new GetObjectRequest(config.bucket, f), new File(config.localDir() + "/" + f));
					log.info("Downloaded " + f + " from " + config.bucket);
				}
			}

		} catch (final Exception e) {
			log.warn("Failed to downloaded underlier file: " + e.getMessage());
			e.printStackTrace();
		}

	}

	public void putFile(final File file, final String name) {

		Observable.from(file).subscribe(new Action1<File>() {

			@Override
			public void call(File t1) {

				final PutObjectResult response =
						s3.putObject(config.bucket(), name == null ? file.getName() : name, file);

				if (response == null) {
					log.error("failed to upload to S3 bucket");
				} else {
					log.debug("load file result: " + response.toString());
				}

			}

		});

	}
}
