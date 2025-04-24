package io.repsy.storage.api;

import java.io.IOException;
import java.io.InputStream;

public interface StorageService {
  void write(String path, InputStream data) throws IOException;
  InputStream read(String path) throws IOException;
  boolean exists(String path);
}
