package com.cocus.codechallenge.repositories.impl.s3;

import com.cocus.codechallenge.models.File;
import com.cocus.codechallenge.repositories.FileRepository;
import com.cocus.codechallenge.repositories.impl.s3.config.S3Properties;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.ByteArrayInputStream;
import java.nio.file.FileAlreadyExistsException;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Slf4j
@Lazy
@Repository("s3FileRepository")
public class S3FileRepository implements FileRepository {

    private final S3Client s3Client;

    private final S3Properties s3Properties;

    private static final String BUCKET_ALREADY_OWNED_BY_YOU_MSG = "BucketAlreadyOwnedByYou";

    private static final String FAIL_TO_CREATE_S3_BUCKET_MSG_TMP = "Fail to create S3 bucket - {}";


    public S3FileRepository(S3Properties s3Properties) {
        this.s3Properties = s3Properties;
        var awsCredentials = AwsBasicCredentials.create(s3Properties.getAccessKeyId(), s3Properties.getSecretAccessKey());
        this.s3Client = S3Client.builder()
                .region(Region.of(s3Properties.getRegion()))
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .build();
    }

    @PostConstruct
    public void init() {
        createBucketIfNotExists();
    }

    @Override
    public boolean save(String fileName, byte[] fileData) {
        try {
            if (objectAlreadyExist(fileName)) throw new FileAlreadyExistsException("File already exists");
            var inputStream = new ByteArrayInputStream(fileData);
            var putObjectRequest = PutObjectRequest.builder()
                    .bucket(this.s3Properties.getBucketName())
                    .key(fileName)
                    .build();
            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, fileData.length));
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Fail to save File - " + e.getMessage());
        }
    }

    @Override
    public List<File> getAll() {
        try {
            var listObjectsV2Request = ListObjectsV2Request.builder()
                    .bucket(this.s3Properties.getBucketName())
                    .build();
            var listObjectsV2Response = s3Client.listObjectsV2(listObjectsV2Request);
            return listObjectsV2Response.contents().stream()
                    .map(s3Object -> new File(s3Object.key(), getObjectContent(s3Object.key())))
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Fail to get all Files - " + e.getMessage());
        }
    }

    @Override
    public Optional<File> getLatest() {
        try {
            var listObjectsV2Request = ListObjectsV2Request.builder()
                    .bucket(this.s3Properties.getBucketName())
                    .build();
            var listObjectsV2Response = s3Client.listObjectsV2(listObjectsV2Request);
            return listObjectsV2Response.contents().stream()
                    .max((o1, o2) -> o1.lastModified().compareTo(o2.lastModified()))
                    .map(s3Object -> new File(s3Object.key(), getObjectContent(s3Object.key())));
        } catch (Exception e) {
            throw new RuntimeException("Fail to get latest File - " + e.getMessage());
        }
    }

    @Override
    public Optional<File> getRandom() {
        try {
            var files = getAll();
            if (files.isEmpty()) {
                return Optional.empty();
            }
            var random = new Random();
            return Optional.of(files.get(random.nextInt(files.size())));
        } catch (Exception e) {
            throw new RuntimeException("Fail to get a random File - " + e.getMessage());
        }
    }


    /**
     * Private Methods
     */

    private boolean objectAlreadyExist(String key) {
        try {
            HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                    .bucket(this.s3Properties.getBucketName())
                    .key(key)
                    .build();
            s3Client.headObject(headObjectRequest);
            return true; // If no exception is thrown, the object exists
        } catch (NoSuchKeyException e) {
            return false; // The object does not exist
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private byte[] getObjectContent(String key) {
        var getObjectRequest = GetObjectRequest.builder()
                .bucket(this.s3Properties.getBucketName())
                .key(key)
                .build();
        return s3Client.getObject(getObjectRequest, ResponseTransformer.toBytes()).asByteArray();
    }

    private void createBucketIfNotExists() {
        try {
            var listBucketsResponse = s3Client.listBuckets(ListBucketsRequest.builder().build());
            boolean bucketExists = listBucketsResponse.buckets().stream()
                    .anyMatch(b -> b.name().equals(this.s3Properties.getBucketName()));
            if (!bucketExists) {
                CreateBucketRequest createBucketRequest = CreateBucketRequest.builder()
                        .bucket(this.s3Properties.getBucketName())
                        .build();
                s3Client.createBucket(createBucketRequest);
            }
        } catch (S3Exception e) {
            if (e.awsErrorDetails().errorCode().equals(BUCKET_ALREADY_OWNED_BY_YOU_MSG)) {
                log.info("Bucket already exists");
            } else {
                log.warn(FAIL_TO_CREATE_S3_BUCKET_MSG_TMP, e.getMessage());
            }
        } catch (Exception e) {
            log.warn(FAIL_TO_CREATE_S3_BUCKET_MSG_TMP, e.getMessage());
        }
    }
}
