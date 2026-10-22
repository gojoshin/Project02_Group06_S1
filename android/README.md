# MovieBox Android App

The Android client for MovieBox, built using **Kotlin**, **Jetpack Compose**, **Material 3**, and **MVVM Architecture**.

---

## 📱 App Architecture & Tech Stack

* **UI Framework**: Jetpack Compose with Material 3 components and custom typography (`Display`, `Mono`).
* **Architecture**: Single Activity (`MainActivity`) with state management driven by `ViewModel`, `StateFlow`, and `collectAsStateWithLifecycle`.
* **Authentication**:
  * **Google Credential Manager**: Uses `androidx.credentials` and `com.google.android.libraries.identity.googleid` for native Google Sign-In popups.
  * **Password Auth**: Interactive tabbed Sign-In & Account Creation UI.
* **Networking**: Retrofit 2 with Gson converter and Coroutines.
* **Image Loading**: Coil (`io.coil-kt:coil-compose`) for high-performance poster loading.

---

## 🗺️ Screen Breakdown

1. **Login & Registration Screen (`LoginScreen.kt`)**:
   * Tabbed toggle between **SIGN IN** and **CREATE ACCOUNT**.
   * Outlined text fields for Email/Username, Display Name, and Password.
   * Native **LOGIN WITH GOOGLE** button via Credential Manager.
   * **DEV / TEST BYPASS LOGIN (ADMIN)** button for rapid emulator testing.
2. **Home Landing Screen (`LandingScreen.kt`)**:
   * Horizontal poster carousel for popular movies.
   * Recently added movies list with titles, directors, and release years.
3. **Movie Detail Screen (`MovieDetailScreen.kt`)**:
   * Backdrop image, title, runtime, synopsis, and rating.
   * User reviews list for the selected movie.
4. **Profile Screen (`ProfileScreen.kt`)**:
   * Account details card displaying `displayName`, `email`, and `role` (`ROLE: USER` or `ROLE: ADMIN`).
   * **SIGN OUT** button.
5. **Admin Console (`AdminScreen.kt`)**:
   * Exclusive tab visible **only to Admin users**.
   * Lists all registered user accounts in the system.
   * **MAKE ADMIN** button to promote standard users.
   * **DELETE** button to permanently delete user accounts from the database.

---

## 🛠️ Building & Running

### Requirements
* Android Studio (Ladybug / 2024.2.1+)
* Android SDK 34/35/37 with minSdk 24 (Android 7.0+)

### Commands
```bash
# Compile Kotlin source code
./gradlew compileDebugKotlin

# Build and install debug APK onto connected emulator/device
./gradlew installDebug
```
