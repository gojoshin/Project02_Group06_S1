package com.example.moviebox.data

import retrofit2.http.Body
import retrofit2.http.POST

data class GoogleLoginRequest(
    val idToken: String
)

data class GoogleLoginResponse(
    val status: String,
    val displayName: String?
)

interface AuthApi {

    @POST("api/v1/auth/google")
    suspend fun googleLogin(
        @Body request: GoogleLoginRequest
    ): GoogleLoginResponse
}