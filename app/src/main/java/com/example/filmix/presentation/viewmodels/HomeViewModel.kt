package com.example.filmix.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmix.data.manager.AppPreloadManager
import com.example.filmix.domain.usecases.base.Result
import com.example.filmix.domain.usecases.home.GetHomeContentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getHomeContentUseCase: GetHomeContentUseCase,
    private val appPreloadManager: AppPreloadManager
) : ViewModel() {

    private val _homeContent = MutableLiveData<Result<GetHomeContentUseCase.HomeContent>>()
    val homeContent: LiveData<Result<GetHomeContentUseCase.HomeContent>> = _homeContent

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object {
        private const val TAG = "HomeViewModel"
    }

    fun loadCachedDataIfAvailable() {
        Log.d(TAG, "🔍 Checking for cached data...")

        if (appPreloadManager.isDataReady()) {
            Log.d(TAG, "📋 Using cached data from preload")


            val homeContent = GetHomeContentUseCase.HomeContent(
                bannerMovies = appPreloadManager.getBannerMoviesCached(),
                popularMovies = appPreloadManager.getPopularMoviesCached(),
                popularTvShows = appPreloadManager.getPopularTvShowsCached(),
                newReleases = appPreloadManager.getNowPlayingMoviesCached(),
                topRated = appPreloadManager.getTopRatedMoviesCached()
            )

            _homeContent.value = Result.Success(homeContent)
            _isLoading.value = false

            Log.d(TAG, "Cached data loaded successfully")
        } else {
            Log.d(TAG, "Cached data not ready, loading fresh data")
            loadHomeContent()
        }
    }

    fun loadHomeContent(forceRefresh: Boolean = false) {
        Log.d(TAG, "Loading home content (forceRefresh: $forceRefresh)")

        viewModelScope.launch {
            _isLoading.value = true

            getHomeContentUseCase(GetHomeContentUseCase.Params(forceRefresh))
                .collect { result ->
                    Log.d(TAG, "Received home content result: $result")
                    _homeContent.value = result
                    _isLoading.value = result is Result.Loading
                }
        }
    }

    fun refreshContent() {
        Log.d(TAG, "Refreshing home content...")
        loadHomeContent(forceRefresh = true)
    }
}
