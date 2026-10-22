package com.example.moviebox.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

data class TmdbMovieDto(
    val id: Long,
    val title: String,
    val overview: String?,
    val releaseDate: String?, // e.g. "2026-01-01"
    val posterPath: String?,
    val backdropPath: String?,
    val voteAverage: Double?,
    val runtime: Int?
)

fun TmdbMovieDto.toMovie(): Movie {
    val year = releaseDate?.split("-")?.firstOrNull()?.toIntOrNull() ?: 2024
    
    val fullPosterUrl = if (posterPath != null) {
        if (posterPath.startsWith("http")) posterPath else "https://image.tmdb.org/t/p/w500$posterPath"
    } else {
        null
    }
    
    return Movie(
        id = id.toInt(),
        title = title,
        year = year,
        director = "Unknown Director", // TmdbMovieDto doesn't return director
        posterColor = 0xFF33363D,
        posterUrl = fullPosterUrl
    )
}

interface MovieApi {
    @GET("api/v1/movies/popular")
    suspend fun getPopularMovies(): List<TmdbMovieDto>
}

object MovieRetrofit {
    private const val BASE_URL = "https://moviebox-backend-62qk.onrender.com/"

    val api: MovieApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(MovieApi::class.java)
}
