# 📄 README - Insurance Policy Kafka Service (Quarkus)

## ✉️ Pendahuluan

Repositori ini adalah implementasi studi kasus backend untuk proses rekrutmen yang diminta oleh Tim Digitalisasi Proses Bisnis. Aplikasi ini dibangun menggunakan Quarkus dan Java 17 dengan integrasi Kafka dan PostgreSQL. Aplikasi berfungsi sebagai layanan backend untuk menerima pesan dari Kafka, memanipulasi data polis asuransi, dan menyimpannya ke database.

## 🧾 Ringkasan Studi Kasus

### 🎯 Tujuan:

Membangun layanan backend yang:

1. Menerima data dari Kafka topic
2. Melakukan manipulasi data sesuai kebutuhan
3. Menulis ulang ke Kafka atau menyimpan hasilnya ke database


## ⚙️ Teknologi yang Digunakan

* **Bahasa Pemrograman:** Java 17
* **Framework:** Quarkus
* **Messaging:** Apache Kafka
* **Database:** PostgreSQL
* **Build Tool:** Maven Wrapper
* **Containerization:** Docker & Docker Compose

## 📂 Struktur Proyek

```
├── src
│   ├── main
│   │   ├── java/com/gitlab/seemugia1/kafkademo
│   │   │   ├── domain           # Model domain seperti InsurancePolicy dan Event
│   │   │   ├── repository       # Interface & implementasi repository
│   │   │   ├── resource         # REST API endpoint untuk InsurancePolicy
│   │   │   └── service          # Kafka consumer dan business logic
│   │   └── resources            # Konfigurasi aplikasi (application.properties)
│   └── test                    # Integration & unit test
├── docker-compose.yml          # Setup untuk PostgreSQL, Kafka, dan app
├── src/main/docker/Dockerfile.jvm # Dockerfile dengan build otomatis
├── pom.xml                     # Dependency Maven
└── README.md                   # Dokumentasi ini
```

## 📡 Arsitektur & Alur Data

1. **Producer** mengirimkan pesan JSON ke Kafka topic `policy-consumer`
2. **InsurancePolicyKafkaConsumer** menerima pesan dan mendeserialisasi menjadi objek `InsurancePolicyEvent`
3. Berdasarkan nilai `action`, data diproses untuk `create`, `update`, atau `delete`
4. Data disimpan atau dimodifikasi di PostgreSQL menggunakan JPA
5. Endpoint REST disediakan untuk CRUD & pengujian manual

## 🔌 API Endpoint

Semua endpoint berbasis RESTful dan berada di `/policies`

### POST /policies

Membuat polis asuransi baru

```json
{
    "policyHolderName": "John Doe",
    "coverageAmount": 2000000.00,
    "premium": 9000.00,
    "startDate": "2025-01-01",
    "endDate": "2026-01-01",
    "status": "INACTIVE"
  }
```


### PUT /policies/{id}

Mengambil polis berdasarkan ID

```json
{
    "policyHolderName": "John Doe",
    "coverageAmount": 2000000.00,
    "premium": 9000.00,
    "startDate": "2025-01-01",
    "endDate": "2026-01-01",
    "status": "INACTIVE"
  }
```


### GET /policies

Mengambil semua polis

### GET /policies/{id}

Mengambil polis berdasarkan ID

### DELETE /policies/{id}

Menghapus polis berdasarkan ID

## ▶️ Menjalankan Aplikasi

Cukup jalankan:

```bash
docker-compose up --build
```

Tidak perlu menjalankan `./mvnw` secara manual karena Dockerfile telah menangani proses build secara otomatis.

## 📥 Kafka Topik

* **Topik Masuk:** `policy-consumer`
* Format pesan:

```json
{
  "action": "created", // atau "updated", "deleted"
  "policy": {
    "id": 1,
    "policyHolderName": "Alice",
    "coverageAmount": 100000,
    "premium": 100000,
    "status": "ACTIVE"
  }
}
```

## 📝 Catatan Tambahan

* Format pesan Kafka harus valid JSON
* Tidak ada autentikasi pada tahap ini (asumsi untuk studi kasus)

## 🙏 Penutup

Terima kasih atas kesempatan yang diberikan. Aplikasi ini dibangun dengan asumsi dan improvisasi kreatif sesuai instruksi studi kasus.
