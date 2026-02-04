# distributed-url-shortener

A high-performance, distributed short link generation and redirection service. This project focuses on solving the core challenges of a URL shortener at scale, including distributed ID management, idempotency, and low-latency redirection.

## Technical Highlights

* **Distributed ID Generation**: Utilizes a Segment-based ID generator to provide unique, 64-bit identifiers without the bottleneck of a single database auto-increment.
* **Idempotency via Fingerprinting**: Implements **MurmurHash3 (32-bit)** to generate URL fingerprints. This prevents the system from wasting IDs and storage space when the same URL is submitted multiple times.
* **ID Shuffling**: Applies bitwise operations and multipliers to raw IDs to ensure that sequential inputs result in non-sequential, unpredictable short codes.
* **Base62 Encoding**: Converts shuffled IDs into a compact string representation using `[0-9][a-z][A-Z]`.
* **High-Performance Caching**: Employs a dual-cache strategy in Redis for  redirection and  idempotency lookups.

## Architecture Workflow

1. **Fingerprint Calculation**: The system computes a MurmurHash3 hash of the long URL.
2. **Idempotency Check**: The system queries Redis using the fingerprint. If a mapping already exists, the existing short code is returned immediately.
3. **ID Consumption**: If the fingerprint is new, the system fetches a unique ID from the distributed generator.
4. **Shuffling & Encoding**: The ID is shuffled and converted to a Base62 string.
5. **Persistence**: The mapping is saved to MySQL and cached in Redis with a TTL synchronized to the link's expiration time.

## Tech Stack

* **Core Framework**: Spring Boot 3.3.2
* **Data Access**: MyBatis-Plus
* **Storage**: MySQL 8.0
* **Cache**: Redis
* **Infrastructure**: Docker & Docker Compose
* **Utilities**: Google Guava (Hashing), Lombok

## Infrastructure (Docker Compose)

The project includes a `docker-compose.yml` to set up the required environment automatically. This ensures consistency across different development machines.

### Services Included:

* **MySQL 8.0**: Persistent storage for short link mappings.
* **Redis**: High-speed cache for redirection and idempotency fingerprints.
* **Short-Link Service**: The Spring Boot application.

## Quick Start

### 1. Prerequisites

Ensure you have **Docker** and **Docker Compose** installed.

### 2. Launch Infrastructure

Run the following command in the project root to start MySQL and Redis:

```bash
docker-compose up -d

```

### 3. Run the Application

You can either run the application locally or build it as a container:

```bash
mvn clean package
java -jar target/short-link-0.0.1-SNAPSHOT.jar

```

## API Reference

### Create a Short Link

`POST /api/v1/short-link`

**Request Body**

```json
{
  "longUrl": "https://example.com/very/long/path/to/resource",
  "expirationTime": "2026-12-31T23:59:59"
}

```

**Response Body**

```json
{
  "shortCode": "7x9aWz",
  "shortUrl": "https://lyn.ly/7x9aWz",
  "expirationTime": "2026-12-31T23:59:59"
}

```
