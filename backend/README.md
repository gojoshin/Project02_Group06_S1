# MovieBox Spring Boot Backend Service

The backend REST API service for MovieBox, built using **Java 21**, **Spring Boot 3.4.2**, **Spring Data JPA**, and **Spring Security**.

---

## 🛠️ Tech Stack & Key Libraries

* **Java Version**: Java 21 LTS
* **Framework**: Spring Boot 3.4.2
* **Persistence & Database**:
  * Spring Data JPA / Hibernate
  * In-memory **H2 Database** (`default` profile for local testing)
  * Managed **Render PostgreSQL** (`render` profile over SSL)
* **Security & Auth**:
  * Spring Security with stateless session management
  * BCrypt password hashing (`BCryptPasswordEncoder`)
  * `com.google.api-client:google-api-client` (v2.7.2) for Google ID Token verification
* **Containerization**: Multi-stage Docker build optimized for cloud deployment.

---

## 🔑 Core REST API Endpoints

### Authentication (`/api/v1/auth`)
* `POST /api/v1/auth/login`: Accepts Google ID token, verifies signature, provisions/finds user account.
* `POST /api/v1/auth/register`: Creates new user account with BCrypt password hash.
* `POST /api/v1/auth/login/password`: Verifies email and BCrypt password credentials.

### Catalog & Reviews (`/api/v1/movies` & `/api/v1/reviews`)
* `GET /api/v1/movies/popular`: Returns list of popular movies with posters and details.
* `GET /api/v1/movies/{id}`: Returns movie details.
* `GET /api/v1/movies/{id}/reviews`: Returns reviews for a movie.
* `POST /api/v1/reviews`: Creates a new review for a movie or show.

### Admin User Management (`/api/v1/users`)
* `GET /api/v1/users`: Returns paginated list of all registered users (Admin RBAC protected).
* `PATCH /api/v1/users/{id}`: Updates user role (`USER` or `ADMIN`).
* `DELETE /api/v1/users/{id}`: Permanently deletes user account from database.

---

## ⚙️ Spring Profiles & Environment Variables

| Variable | Default Value | Description |
| :--- | :--- | :--- |
| `SPRING_PROFILES_ACTIVE` | `default` | Set to `render` for Render PostgreSQL profile |
| `PORT` | `8080` | Port for Spring Boot server binding |
| `TMDB_API_KEY` | `demo_key` | TMDB API Key for fetching movie catalog |
| `GOOGLE_WEB_CLIENT_ID` | `demo_client_id` | OAuth Web Client ID for Google ID Token validation |
| `RENDER_DB_HOST` | `localhost` | PostgreSQL host for Render profile |
| `RENDER_DB_NAME` | `moviebox_db` | PostgreSQL database name |
| `RENDER_DB_USER` | `postgres` | PostgreSQL username |
| `RENDER_DB_PASSWORD` | *(Secret)* | PostgreSQL password |

---

## 🧪 Testing & Building

```bash
# Run unit tests
./gradlew test

# Build executable Spring Boot JAR
./gradlew bootJar --no-daemon -x test
```
