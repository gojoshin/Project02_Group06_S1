package com.example.moviebox.data

import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

// The backend sends TMDB's field names with underscores (release_date,
// poster_path, ...). @SerializedName tells Gson which JSON name goes with
// which Kotlin field. Without it these all came back null, which is why
// posters were blank and every year showed 2024.
data class TmdbMovieDto(
    val id: Long,
    val title: String,
    val overview: String?,
    @SerializedName("release_date") val releaseDate: String?, // e.g. "2026-01-01"
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("backdrop_path") val backdropPath: String?,
    @SerializedName("vote_average") val voteAverage: Double?,
    val runtime: Int?
)

// Page info from the backend's PageMetaDto
data class PageMeta(
    val size: Int,
    val number: Int,          // which page this is, starting at 1
    val totalElements: Long,  // how many movies there are in total
    val totalPages: Int
)

// One page of movies from GET /api/v1/movies
data class MoviePage(
    val content: List<TmdbMovieDto>,
    val page: PageMeta
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
    // One page of movies for Explore. Leave query null for popular movies,
    // or pass what the user typed to search by title.
    @GET("api/v1/movies")
    suspend fun getMovies(
        @Query("page") page: Int,
        @Query("query") query: String? = null
    ): MoviePage

    @GET("api/v1/movies/popular")
    suspend fun getPopularMovies(): List<TmdbMovieDto>

    @GET("api/v1/movies/{movieId}")
    suspend fun getMovie(@retrofit2.http.Path("movieId") movieId: Long): TmdbMovieDto
}

object MovieRetrofit {
    private const val BASE_URL = "http://127.0.0.1:8080/"

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .build()

    val api: MovieApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(MovieApi::class.java)
}