package com.example.moviebox.data

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

// One saved favorite.
data class FavoriteItem(
    val id: Long,
    val mediaType: String,
    val mediaId: Long
)

// The favorites routes from FavoriteController.
interface FavoritesApi {

    @GET("api/v1/favorites")
    suspend fun getFavorites(): List<FavoriteItem>

    @PUT("api/v1/favorites/{mediaType}/{mediaId}")
    suspend fun addFavorite(
        @Path("mediaType") mediaType: String,
        @Path("mediaId") mediaId: Long
    ): FavoriteItem

    //  check whether it worked
    @DELETE("api/v1/favorites/{mediaType}/{mediaId}")
    suspend fun removeFavorite(
        @Path("mediaType") mediaType: String,
        @Path("mediaId") mediaId: Long
    ): Response<Unit>
}
object FavoritesRetrofit {

    val api: FavoritesApi = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8080/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(FavoritesApi::class.java)
}