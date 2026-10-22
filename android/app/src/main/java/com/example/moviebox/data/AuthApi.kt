package com.example.moviebox.data

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

data class LoginRequest(
    val idToken: String
)

data class User(
    val id: Long,
    val email: String,
    val displayName: String,
    val role: String
)

data class UserPageResponse(
    val content: List<User>
)

interface AuthApi {

    @POST("api/v1/auth/login")
    suspend fun loginWithGoogle(
        @Body request: LoginRequest
    ): User

    @GET("api/v1/users")
    suspend fun listUsers(): UserPageResponse

    @PATCH("api/v1/users/{userId}")
    suspend fun updateUserRole(
        @Path("userId") userId: Long,
        @Body patch: Map<String, String>
    ): User

    @DELETE("api/v1/users/{userId}")
    suspend fun deleteUser(
        @Path("userId") userId: Long
    )
}