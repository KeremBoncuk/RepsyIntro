# Repsy Intro Spring Application

## Overview
This project implements a minimal package-repository service for the **Repsy**.
It exposes a REST API to upload and download compiled `.rep` (zip) packages and their `meta.json` descriptors, stores the files via a pluggable storage layer (file system or MinIO object storage), and persists package metadata in PostgreSQL.

## Project Modules & Libraries
| Module / Library       | Purpose (concise)                                               |
|------------------------|-----------------------------------------------------------------|
| **storage-api**        | Declares `StorageService` interface (`write`, `read`, `exists`).|
| **filesystem-storage** | Implements `StorageService` via the local file system.          |
| **minio-storage**      | Implements `StorageService` using a MinIO/S3 bucket.            |
| **repsy-spring**       | Spring Boot app: REST controllers, JPA entities, configuration. |

## How It Works
1. **Upload** – `POST /{packageName}/{version}`  
   Streams `package.rep` and `meta.json` to the chosen storage layer; metadata is recorded in PostgreSQL.  
2. **Download** – `GET /{packageName}/{version}/{fileName}`  
   Retrieves a stored file (`package.rep` or `meta.json`).  
3. **List** – `GET /packages`  
   Returns all stored package records.

Activate the storage strategy at runtime with `STORAGE_STRATEGY` (`file-system` or `object-storage`).

## REST API Endpoints
| Method | Endpoint                                   | Description                       |
|--------|--------------------------------------------|-----------------------------------|
| POST   | `/{packageName}/{version}`                 | Upload `.rep` + `meta.json`       |
| GET    | `/{packageName}/{version}/{fileName}`      | Download a stored file            |
| GET    | `/packages`                                | List all packages (database view) |

## Docker Usage
Docker Container is in Repsy Repository:

https://repsy.io/keremboncuk/docker/repsyintro_public_kb

You can run the docker file using:

docker run -p 8080:8080 \
  -e STORAGE_STRATEGY=file-system \
  repo.repsy.io/keremboncuk/repsyintro_public_kb/repsy-spring:latest

  Note: In order for the docker file to run it needs postgre (-p 5432) and minio (-p 9000) to run

### Environment Variables
Possible Environment Variables and their default values

STORAGE_STRATEGY	--> file-system or object-storage	file-system

STORAGE_FILESYSTEM_BASE_DIR	--> Directory for local storage	uploads

STORAGE_MINIO_URL	--> MinIO endpoint	(by default it is http://minio:9000)

MINIO_ACCESS_KEY / MINIO_SECRET_KEY -->	MinIO credentials	(by default they are minioadmin / minioadmin)

MINIO_BUCKET	--> MinIO bucket name	(by defaukt it is repsy-bucket)

POSTGRES_USER / POSTGRES_PASSWORD	--> PostgreSQL credentials	(by default it is admin / admin)

To manage these environment variables feel free to use and change the values:

docker run -p 8080:8080 \
  -e STORAGE_STRATEGY=file-system \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/repsydb \
  -e SPRING_DATASOURCE_USERNAME=repsyuser \
  -e SPRING_DATASOURCE_PASSWORD=repsypass \
  repo.repsy.io/keremboncuk/repsyintro_public_kb/repsy-spring:latest


