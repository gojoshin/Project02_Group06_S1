package com.example.moviebox.data

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

// One saved favorite, matching FavoriteDto on the backend.
data class FavoriteItem(
    val id: Long,
    val mediaType: String,
    val mediaId: Long
)

// The favorites routes from FavoriteController. Every call includes the
// logged in user's id so each account gets its own list.
interface FavoritesApi {

    @GET("api/v1/users/{userId}/favorites")
    suspend fun getFavorites(
        @Path("userId") userId: Long
    ): List<FavoriteItem>

    @PUT("api/v1/users/{userId}/favorites/{mediaType}/{mediaId}")
    suspend fun addFavorite(
        @Path("userId") userId: Long,
        @Path("mediaType") mediaType: String,
        @Path("mediaId") mediaId: Long
    ): FavoriteItem

    @DELETE("api/v1/users/{userId}/favorites/{mediaType}/{mediaId}")
    suspend fun removeFavorite(
        @Path("userId") userId: Long,
        @Path("mediaType") mediaType: String,
        @Path("mediaId") mediaId: Long
    ): Response<Unit>
}

object FavoritesRetrofit {

    private const val BASE_URL = "https://moviebox-backend-62qk.onrender.com/"

    val api: FavoritesApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(FavoritesApi::class.java)
}