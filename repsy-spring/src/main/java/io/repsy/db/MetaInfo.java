package io.repsy.db;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MetaInfo {

  @JsonProperty("packageName")
  private String packageName;

  @JsonProperty("version")
  private String version;

  @JsonProperty("description")
  private String description;

  public MetaInfo() {}

  public MetaInfo(String packageName, String version, String description) {
    this.packageName = packageName;
    this.version = version;
    this.description = description;
  }

  public String getPackageName() {
    return packageName;
  }

  public void setPackageName(String packageName) {
    this.packageName = packageName;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
