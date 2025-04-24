package io.repsy.storage.filesystem;

import io.repsy.storage.api.StorageService;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;

public class FileSystemStorageService implements StorageService {

  private final Path basePath;

  public FileSystemStorageService(String baseDir) throws IOException {
    this.basePath = Paths.get(baseDir);
    Files.createDirectories(basePath);
  }

  @Override
  public void write(String path, InputStream data) throws IOException {
    Path target = basePath.resolve(path);
    Files.createDirectories(target.getParent());
    Files.copy(data, target, StandardCopyOption.REPLACE_EXISTING);
  }

  @Override
  public InputStream read(String path) throws IOException {
    return Files.newInputStream(basePath.resolve(path));
  }

  @Override
  public boolean exists(String path) {
    return Files.exists(basePath.resolve(path));
  }
}
