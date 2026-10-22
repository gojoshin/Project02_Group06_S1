package com.example.moviebox.data

/*
 * Notes:
 *
 * Start the Spring Boot backend on port 8080.
 *
 * For Android emulator testing, run the following:
 * adb reverse tcp:8080 tcp:8080
 *
 * in backend and then:
 *
 * ..\android\gradlew.bat bootRun
 *
 * If that doesn't work, use http://10.0.2.2:8080/
 * if the emulator can reach the host directly.
 *
 * Once the backend is deployed, should replace BASE_URL
 * with the deployed HTTPS URL.
 */

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AuthRetrofit {
    var api: AuthApi = Retrofit.Builder()
        .baseUrl("https://moviebox-backend-62qk.onrender.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(AuthApi::class.java)
}