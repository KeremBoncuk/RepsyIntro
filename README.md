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
Note: docker-compose.yml file is inside repsy-spring directory.

However you can check out the containerized version of this app in: https://repsy.io/keremboncuk/docker/repsyintro_public_kb feel free to check it out. It comes with a docker-compose.yml 

### 1. Build
docker-compose build


### 2. Run
docker-compose up

Starts repsy-spring, postgres, and (if object-storage) minio.

### 3. Environment Variables
Variable	Description	Default

STORAGE_STRATEGY	--> file-system or object-storage	file-system

STORAGE_FILESYSTEM_BASE_DIR	--> Directory for local storage	uploads

STORAGE_MINIO_URL	--> MinIO endpoint	(by default it is http://minio:9000)

MINIO_ACCESS_KEY / MINIO_SECRET_KEY -->	MinIO credentials	(by default they are minioadmin / minioadmin)

MINIO_BUCKET	--> MinIO bucket name	(by defaukt it is repsy-bucket)

POSTGRES_USER / POSTGRES_PASSWORD	--> PostgreSQL credentials	(by default it is admin / admin)

 - You can change these default values from docker-compose.yml file which is inside "repsy-spring" module
 - OR you can mention these parameter on command as follows:
 - STORAGE_STRATEGY=object-storage docker-compose up

Linux / macOS	Install Docker & Docker Compose; run commands above (preface with sudo if needed).
Windows	Use Docker Desktop with WSL 2 backend; run commands in PowerShell, CMD, or Git Bash.

Configuration files are identical across platforms.
