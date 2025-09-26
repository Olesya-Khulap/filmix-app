package com.example.filmix.domain.repository

import com.example.filmix.domain.models.*
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    // Movie methods
    suspend fun getPopularMovies(page: Int, forceRefresh: Boolean = false): List<MovieItem>
    suspend fun getTopRatedMovies(page: Int, forceRefresh: Boolean = false): List<MovieItem>
    suspend fun getNowPlayingMovies(page: Int, forceRefresh: Boolean = false): List<MovieItem>
    suspend fun getBannerMovies(forceRefresh: Boolean = false): List<MovieItem>
    suspend fun getMovieDetails(movieId: Int, forceRefresh: Boolean = false): MovieDetails
    suspend fun getMovieImages(movieId: Int, forceRefresh: Boolean = false): ImageResponse
    suspend fun getMovieVideos(movieId: Int, forceRefresh: Boolean = false): VideoResponse
    suspend fun getMovieCredits(movieId: Int, forceRefresh: Boolean = false): CreditsResponse

    // TV Show methods
    suspend fun getPopularTvShows(page: Int, forceRefresh: Boolean = false): List<MovieItem>
    suspend fun getTvDetails(tvId: Int, forceRefresh: Boolean = false): MovieDetails
    suspend fun getTvImages(tvId: Int, forceRefresh: Boolean = false): ImageResponse
    suspend fun getTvVideos(tvId: Int, forceRefresh: Boolean = false): VideoResponse
    suspend fun getTvCredits(tvId: Int, forceRefresh: Boolean = false): CreditsResponse

    // Search methods
    suspend fun searchMulti(query: String, page: Int = 1): List<MovieItem>

    // Genre methods
    suspend fun getMovieGenres(forceRefresh: Boolean = false): List<Genre>
    suspend fun getTvGenres(forceRefresh: Boolean = false): List<Genre>

    // Person methods
    suspend fun getPersonDetails(personId: Int): PersonDetails
    suspend fun getPersonFilmography(personId: Int): List<MovieItem>

    // Flow methods
    fun getPopularMoviesFlow(): Flow<List<MovieItem>>
    fun getCachedMovieById(movieId: Int): Flow<MovieItem?>

    // Cache management
    suspend fun clearOldCache()

    suspend fun getCountries(): List<Country>
    suspend fun getFilteredMovies(page: Int, filters: MovieFilters): List<MovieItem>

}
