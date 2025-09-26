package com.example.filmix.data.manager

import android.util.Log
import com.example.filmix.domain.models.MovieItem
import com.example.filmix.domain.usecases.base.Result
import com.example.filmix.domain.usecases.movie.GetPopularMoviesUseCase
import com.example.filmix.domain.usecases.movie.GetTopRatedMoviesUseCase
import com.example.filmix.domain.usecases.movie.GetNowPlayingMoviesUseCase
import com.example.filmix.domain.usecases.tvshow.GetPopularTvShowsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppPreloadManager @Inject constructor(
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    private val getPopularTvShowsUseCase: GetPopularTvShowsUseCase,
    private val getTopRatedMoviesUseCase: GetTopRatedMoviesUseCase,
    private val getNowPlayingMoviesUseCase: GetNowPlayingMoviesUseCase
) {

    companion object {
        private const val TAG = "AppPreloadManager"
    }

    // Cached data
    private val _popularMovies = MutableStateFlow<List<MovieItem>>(emptyList())
    val popularMovies: StateFlow<List<MovieItem>> = _popularMovies.asStateFlow()

    private val _popularTvShows = MutableStateFlow<List<MovieItem>>(emptyList())
    val popularTvShows: StateFlow<List<MovieItem>> = _popularTvShows.asStateFlow()

    private val _topRatedMovies = MutableStateFlow<List<MovieItem>>(emptyList())
    val topRatedMovies: StateFlow<List<MovieItem>> = _topRatedMovies.asStateFlow()

    private val _nowPlayingMovies = MutableStateFlow<List<MovieItem>>(emptyList())
    val nowPlayingMovies: StateFlow<List<MovieItem>> = _nowPlayingMovies.asStateFlow()

    private val _bannerMovies = MutableStateFlow<List<MovieItem>>(emptyList())
    val bannerMovies: StateFlow<List<MovieItem>> = _bannerMovies.asStateFlow()

    // Loading status
    private val _isDataLoaded = MutableStateFlow(false)
    val isDataLoaded: StateFlow<Boolean> = _isDataLoaded.asStateFlow()

    suspend fun preloadAllData(): Result<Unit> {
        Log.d(TAG, "Starting preload of all data")

        try {

            getPopularMoviesUseCase(GetPopularMoviesUseCase.Params(1))
                .collect { result ->
                    if (result is Result.Success) {
                        _popularMovies.value = result.data
                        Log.d(TAG, "Popular movies cached: ${result.data.size}")

                        _bannerMovies.value = result.data.shuffled().take(5)
                        Log.d(TAG, "Banner movies created: ${_bannerMovies.value.size}")
                    }
                }

            getPopularTvShowsUseCase(GetPopularTvShowsUseCase.Params(1))
                .collect { result ->
                    if (result is Result.Success) {
                        _popularTvShows.value = result.data
                        Log.d(TAG, "Popular TV shows cached: ${result.data.size}")
                    }
                }

            getTopRatedMoviesUseCase(GetTopRatedMoviesUseCase.Params(1))
                .collect { result ->
                    if (result is Result.Success) {
                        _topRatedMovies.value = result.data
                        Log.d(TAG, "Top rated cached: ${result.data.size}")
                    }
                }

            getNowPlayingMoviesUseCase(GetNowPlayingMoviesUseCase.Params(1))
                .collect { result ->
                    if (result is Result.Success) {
                        _nowPlayingMovies.value = result.data
                        Log.d(TAG, "Now playing cached: ${result.data.size}")
                    }
                }

            _isDataLoaded.value = true
            Log.d(TAG, "All data preloaded successfully")
            return Result.Success(Unit)

        } catch (e: Exception) {
            Log.e(TAG, "Error preloading data", e)
            return Result.Error(e.message ?: "Preload failed")
        }
    }

    fun isDataReady(): Boolean = _isDataLoaded.value

    fun getPopularMoviesCached(): List<MovieItem> = _popularMovies.value
    fun getPopularTvShowsCached(): List<MovieItem> = _popularTvShows.value
    fun getTopRatedMoviesCached(): List<MovieItem> = _topRatedMovies.value
    fun getNowPlayingMoviesCached(): List<MovieItem> = _nowPlayingMovies.value
    fun getBannerMoviesCached(): List<MovieItem> = _bannerMovies.value
}
