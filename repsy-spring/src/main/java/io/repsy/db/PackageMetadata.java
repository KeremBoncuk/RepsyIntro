package io.repsy.db;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class PackageMetadata {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String packageName;
  private String version;
  private String description;
  private LocalDateTime uploadedAt;

  public PackageMetadata() {}

  public PackageMetadata(String packageName, String version, String description, LocalDateTime uploadedAt) {
    this.packageName = packageName;
    this.version = version;
    this.description = description;
    this.uploadedAt = uploadedAt;
  }

  public Long getId() {
    return id;
  }

  public String getPackageName() {
    return packageName;
  }

  public String getVersion() {
    return version;
  }

  public String getDescription() {
    return description;
  }

  public LocalDateTime getUploadedAt() {
    return uploadedAt;
  }
}
