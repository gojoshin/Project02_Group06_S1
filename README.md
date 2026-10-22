# MovieBox Monorepo

MovieBox is a full-stack movie and TV show tracking application. It consists of a **Spring Boot 3 / Java 21 REST API backend** and a modern **Jetpack Compose Android mobile application**.

---

## 🎬 Project Overview & Features

MovieBox allows users to discover popular movies, log ratings and reviews, track watch progress, and manage user accounts.

* **Movie & TV Show Discovery**: Integrates with The Movie Database (TMDB) API for real-time popular movies, details, posters, and streaming availability.
* **Review & Rating System**: Users can rate movies (1–5 stars), write detailed reviews, and flag spoilers.
* **Multi-Method Authentication**:
  * **Google OAuth Sign-In**: Powered by Android Credential Manager API and verified cryptographically on the backend using `google-api-client`.
  * **Username & Password Authentication**: Standard account registration and login with BCrypt password hashing.
  * **Dev / Test Bypass**: Quick login for development and testing.
* **Admin Console & Role-Based Access Control (RBAC)**:
  * Automatic assignment of the `ADMIN` role to the first registered user.
  * Admins can view all registered accounts, promote standard users to Admins, and delete user accounts.
* **Cloud Deployment**: Backend hosted on **Render** as a Docker Web Service connected to a managed **Render PostgreSQL** database.

---

## 📁 Repository Structure

This repository is organized as a monorepo containing both the backend service and the Android client:

```text
Project02_Group06_S1/
├── android/               # Jetpack Compose Android Mobile App
│   ├── app/               # App module, UI screens, ViewModels, and Retrofit API client
│   └── README.md          # Android documentation & setup guide
├── backend/               # Spring Boot 3 REST API Service
│   ├── src/               # Controllers, Services, Entities, DTOs, and Repositories
│   ├── Dockerfile         # Multi-stage Docker build file
│   └── README.md          # Backend API documentation & database guide
├── docs/                  # API Contracts & Design Specifications
│   ├── openapi.yaml       # OpenAPI 3.0 API Specification
│   └── proposal.md        # Project pitch, ER diagrams, and technical choices
├── docker-compose.yml     # Local multi-container Docker configuration
└── render.yaml            # Render Blueprint deployment specification
```

---

## 🚀 Quickstart & Local Setup

### Prerequisites
* Java 21 JDK
* Android Studio (Ladybug / 2024.2.1+)
* Docker & Docker Compose

### 1. Running the Backend Locally
You can run the backend service locally using Docker Compose or Gradle:

**Via Docker Compose:**
```bash
# Start local backend container on port 8080
docker compose up --build -d
```

**Via Gradle:**
```bash
cd backend
./gradlew bootRun
```
* **Local API Base URL**: `http://localhost:8080` (or `http://10.0.2.2:8080` from Android Emulator)
* **Local H2 Console**: `http://localhost:8080/h2-console`

### 2. Running the Android Application
1. Open the `android/` directory in Android Studio.
2. Select an Android Emulator (API Level 24+).
3. Click **Run 'app'** or execute:
```bash
cd android
./gradlew installDebug
```

---

## ☁️ Cloud Backend Deployment (Render)

* **Live Web Service Base URL**: `https://moviebox-backend-62qk.onrender.com/`
* **Hosted Database**: Render PostgreSQL over SSL (`sslmode=require`)
* **OpenAPI Contract**: Defined in [`docs/openapi.yaml`](docs/openapi.yaml)
