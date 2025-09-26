package com.example.filmix.data.remote.api

import com.example.filmix.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    // Popular content
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en-US"
    ): Response<MovieListResponse>

    @GET("tv/popular")
    suspend fun getPopularTvShows(
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en-US"
    ): Response<TvShowListResponse>


    // Now playing and top rated
    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en-US"
    ): Response<MovieListResponse>

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en-US"
    ): Response<MovieListResponse>

    // Movie details
    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String = "en-US"
    ): Response<MovieDetailsDto>

    @GET("tv/{tv_id}")
    suspend fun getTvDetails(
        @Path("tv_id") tvId: Int,
        @Query("language") language: String = "en-US"
    ): Response<TvDetailsDto>

    // Images
    @GET("movie/{movie_id}/images")
    suspend fun getMovieImages(
        @Path("movie_id") movieId: Int
    ): Response<ImageResponse>

    @GET("tv/{tv_id}/images")
    suspend fun getTvImages(
        @Path("tv_id") tvId: Int
    ): Response<ImageResponse>

    // Videos
    @GET("movie/{movie_id}/videos")
    suspend fun getMovieVideos(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String = "en-US"
    ): Response<VideoResponse>

    @GET("tv/{tv_id}/videos")
    suspend fun getTvVideos(
        @Path("tv_id") tvId: Int,
        @Query("language") language: String = "en-US"
    ): Response<VideoResponse>

    // Credits
    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCredits(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String = "en-US"
    ): Response<CreditsResponse>

    @GET("tv/{tv_id}/credits")
    suspend fun getTvCredits(
        @Path("tv_id") tvId: Int,
        @Query("language") language: String = "en-US"
    ): Response<CreditsResponse>

    // Search
    @GET("search/multi")
    suspend fun searchMulti(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en-US"
    ): Response<SearchResponse>

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en-US"
    ): Response<MovieListResponse>

    @GET("search/tv")
    suspend fun searchTv(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en-US"
    ): Response<TvShowListResponse>

    // Genres
    @GET("genre/movie/list")
    suspend fun getMovieGenres(
        @Query("language") language: String = "en-US"
    ): Response<GenreListResponse>

    @GET("genre/tv/list")
    suspend fun getTvGenres(
        @Query("language") language: String = "en-US"
    ): Response<GenreListResponse>

    // Countries
    @GET("configuration/countries")
    suspend fun getCountries(): Response<List<CountryDto>>

    // Person details
    @GET("person/{person_id}")
    suspend fun getPersonDetails(
        @Path("person_id") personId: Int,
        @Query("language") language: String = "en-US"
    ): Response<PersonDetailsDto>

    @GET("person/{person_id}/movie_credits")
    suspend fun getPersonMovieCredits(
        @Path("person_id") personId: Int,
        @Query("language") language: String = "en-US"
    ): Response<PersonMovieCreditsDto>

    @GET("person/{person_id}/tv_credits")
    suspend fun getPersonTvCredits(
        @Path("person_id") personId: Int,
        @Query("language") language: String = "en-US"
    ): Response<PersonTvCreditsDto>

    // Discover with filters
    @GET("discover/movie")
    suspend fun discoverMovies(
        @Query("page") page: Int = 1,
        @Query("with_genres") genreIds: String? = null,
        @Query("with_origin_country") countries: String? = null,
        @Query("vote_average.gte") minRating: Double? = null,
        @Query("vote_average.lte") maxRating: Double? = null,
        @Query("primary_release_date.gte") yearFrom: String? = null,
        @Query("primary_release_date.lte") yearTo: String? = null,
        @Query("language") language: String = "en-US"
    ): Response<MovieListResponse>

    @GET("discover/tv")
    suspend fun discoverTvShows(
        @Query("page") page: Int = 1,
        @Query("with_genres") genreIds: String? = null,
        @Query("with_origin_country") countries: String? = null,
        @Query("vote_average.gte") minRating: Double? = null,
        @Query("vote_average.lte") maxRating: Double? = null,
        @Query("first_air_date.gte") yearFrom: String? = null,
        @Query("first_air_date.lte") yearTo: String? = null,
        @Query("with_status") status: String? = null,
        @Query("language") language: String = "en-US"
    ): Response<TvShowListResponse>
}
