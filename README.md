# MovieBox

MovieBox is an application for discovering movies and TV shows, viewing details and streaming providers, and tracking personal watch activity.

This repository is a monorepo containing:

- An Android client built with Kotlin and Jetpack Compose
- A Spring Boot backend that exposes the MovieBox API
- OpenAPI documentation, automated tests, and Render deployment configuration

## Features

- Browse popular movies and explore movie details
- View posters, descriptions, release information, and streaming providers
- Register and log in with an account
- Save favorite movies
- Maintain watch history and episode watch status
- Create, edit, and view reviews
- Track TV show progress by season and episode
- Provide admin-only user management

## Architecture

| Directory | Description |
|---|---|
| `android/` | Android application using Kotlin, Jetpack Compose, Retrofit, and Material 3 |
| `backend/` | Spring Boot REST API using Java 21, Spring Data JPA, validation, and security |
| `docs/openapi.yaml` | OpenAPI 3.0 contract for the API |
| `docs/proposal.md` | Project proposal, data model, risks, and team plan |
| `docker-compose.yml` | Local container configuration for the backend |
| `render.yaml` | Render web service and PostgreSQL deployment configuration |

## API

The production backend is deployed at:

[https://moviebox-backend-62qk.onrender.com](https://moviebox-backend-62qk.onrender.com)

The API uses versioned routes under `/api/v1`. The main resource areas include:

- Movies, shows, episodes, and streaming providers
- Reviews and ratings
- Favorites, watch history, and episode progress
- User accounts and admin user management

See the complete API contract in [`docs/openapi.yaml`](docs/openapi.yaml).

### Authentication

Authentication uses bearer tokens. Protected endpoints require an `Authorization` header:

```http
Authorization: Bearer <token>
```

## Local Backend Setup

### Requirements

- Java 21
- Docker and Docker Compose
- A TMDB API key
- PostgreSQL credentials when using the Render profile

Copy the example environment file and fill in the values locally:

```bash
cp .env.example .env
```

Do not commit `.env` files or any API keys, passwords, or tokens.

### Run with Docker Compose

```bash
docker compose up --build
```

The backend will be available at:

```text
http://localhost:8080
```

### Run Directly with Gradle

```bash
cd backend
./gradlew bootRun
```

The default profile uses an in-memory H2 database. Set `SPRING_PROFILES_ACTIVE=render` when connecting to the Render PostgreSQL configuration.

## Android App

Open the `android/` directory in Android Studio, allow Gradle to sync, and run the app configuration on an emulator or Android device.

A debug APK can be built from the command line with:

```bash
cd android
./gradlew assembleDebug
```

The Android client is configured to communicate with the backend API. When testing against a backend running on the host machine from an Android emulator, use the emulator-accessible host address rather than `localhost`.

## API Contract Tools

Lint the OpenAPI contract:

```bash
npx @redocly/cli lint docs/openapi.yaml
```

Start a mock server from the contract:

```bash
npx @stoplight/prism-cli mock docs/openapi.yaml
```

The mock server listens at:

```text
http://127.0.0.1:4010
```

## Team

- **Jayson** — Frontend UI and database
- **Daniel** — Explore page
- **Joseph** — Episode tracking
- **Joshua** — API and backend

## Project Links

- [Project proposal](docs/proposal.md)
- [OpenAPI contract](docs/openapi.yaml)
