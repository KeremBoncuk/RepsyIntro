package io.repsy.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.repsy.db.MetaInfo;
import io.repsy.db.PackageMetadata;
import io.repsy.db.PackageMetadataRepository;
import io.repsy.storage.api.StorageService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@RestController
public class PackageController {

  private final StorageService storageService;
  private final PackageMetadataRepository repository;
  private final ObjectMapper objectMapper;

  public PackageController(StorageService storageService, PackageMetadataRepository repository) {
    this.storageService = storageService;
    this.repository = repository;
    this.objectMapper = new ObjectMapper();
  }

  @PostMapping("/{packageName}/{version}")
  public ResponseEntity<String> upload(
    @PathVariable("packageName") String packageNamePath,
    @PathVariable("version") String versionPath,
    @RequestParam("meta") MultipartFile metaFile,
    @RequestParam("package") MultipartFile packageFile) {

    try {
      // Parse meta.json
      MetaInfo meta = objectMapper.readValue(metaFile.getInputStream(), MetaInfo.class);

      // Basic validation
      if (meta.getPackageName() == null || meta.getVersion() == null) {
        return ResponseEntity.badRequest().body("Missing required fields in meta.json.");
      }

      // Store files
      String basePath = meta.getPackageName() + "/" + meta.getVersion() + "/";
      storageService.write(basePath + "meta.json", metaFile.getInputStream());
      storageService.write(basePath + "package.rep", packageFile.getInputStream());

      // Save metadata in DB
      repository.save(new PackageMetadata(
        meta.getPackageName(),
        meta.getVersion(),
        meta.getDescription(),
        LocalDateTime.now()
      ));

      return ResponseEntity.ok("Package uploaded and metadata stored successfully.");

    } catch (IOException e) {
      return ResponseEntity.internalServerError().body("Failed to parse or upload package: " + e.getMessage());
    }
  }

  @GetMapping("/{packageName}/{version}/{fileName}")
  public ResponseEntity<InputStreamResource> download(
    @PathVariable("packageName") String packageName,
    @PathVariable("version") String version,
    @PathVariable("fileName") String fileName) {

    String path = packageName + "/" + version + "/" + fileName;

    try {
      if (!storageService.exists(path)) {
        return ResponseEntity.notFound().build();
      }

      return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
        .body(new InputStreamResource(storageService.read(path)));

    } catch (IOException e) {
      return ResponseEntity.internalServerError().build();
    }
  }

  @GetMapping("/packages")
  public ResponseEntity<?> getAllPackages() {
    return ResponseEntity.ok(repository.findAll());
  }

}
