package io.repsy.storage.minio;

import io.minio.*;
import io.repsy.storage.api.StorageService;

import java.io.IOException;
import java.io.InputStream;

public class MinioStorageService implements StorageService {

  private final MinioClient minioClient;
  private final String bucketName;

  public MinioStorageService(String endpoint, String accessKey, String secretKey, String bucketName) throws Exception {
    this.minioClient = MinioClient.builder()
      .endpoint(endpoint)
      .credentials(accessKey, secretKey)
      .build();
    this.bucketName = bucketName;

    boolean exists = minioClient.bucketExists(
      BucketExistsArgs.builder().bucket(bucketName).build()


    );

    if (!exists) {
      minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());

    }
  }

  @Override
  public void write(String path, InputStream data) throws IOException {
    try {

      minioClient.putObject(
        PutObjectArgs.builder()
          .bucket(bucketName)
          .object(path)
          .stream(data, -1, 10485760)
          .build()
      );
    } catch (Exception e) {
      throw new IOException("Minio write failed", e);
    }
  }

  @Override
  public InputStream read(String path) throws IOException {
    try {
      return minioClient.getObject(
        GetObjectArgs.builder()
          .bucket(bucketName)
          .object(path)
          .build()
      );
    } catch (Exception e) {
      throw new IOException("Minio read failed", e);
    }
  }

  @Override
  public boolean exists(String path) {
    try {
      minioClient.statObject(
        StatObjectArgs.builder()
          .bucket(bucketName)
          .object(path)
          .build()
      );
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
