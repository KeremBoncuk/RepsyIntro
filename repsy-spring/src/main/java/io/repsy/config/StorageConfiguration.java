package io.repsy.config;

import io.repsy.storage.api.StorageService;
import io.repsy.storage.filesystem.FileSystemStorageService;
import io.repsy.storage.minio.MinioStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfiguration {

  @Value("${storageStrategy}")
  private String strategy;

  @Value("${storageFilesystemBaseDir:uploads}")
  private String baseDir;

  @Value("${storageMinioUrl}")
  private String minioUrl;

  @Value("${storageMinioAccessKey}")
  private String minioAccessKey;

  @Value("${storageMinioSecretKey}")
  private String minioSecretKey;

  @Value("${storageMinioBucket}")
  private String minioBucket;

  @Bean
  public StorageService storageService() throws Exception {
    System.out.println("⚙️  Using storageStrategy = " + strategy);

    if ("object-storage".equalsIgnoreCase(strategy)) {
      return new MinioStorageService(minioUrl, minioAccessKey, minioSecretKey, minioBucket);
    } else {
      return new FileSystemStorageService(baseDir);
    }
  }
}
