package com.example.filmix.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmix.data.manager.AppPreloadManager
import com.example.filmix.domain.usecases.base.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val appPreloadManager: AppPreloadManager
) : ViewModel() {

    private val _loadingProgress = MutableLiveData<Int>()
    val loadingProgress: LiveData<Int> = _loadingProgress

    private val _loadingText = MutableLiveData<String>()
    val loadingText: LiveData<String> = _loadingText

    private val _navigationReady = MutableLiveData<Boolean>()
    val navigationReady: LiveData<Boolean> = _navigationReady

    companion object {
        private const val TAG = "SplashViewModel"
    }

    fun startPreloading() {
        Log.d(TAG, "Starting data preloading...")
        _loadingProgress.value = 0
        _loadingText.value = "Initializing"

        viewModelScope.launch {
            try {
                if (appPreloadManager.isDataReady()) {
                    Log.d(TAG, "Data already loaded, skipping preload")
                    _loadingProgress.value = 100
                    _loadingText.value = "Ready!"
                    delay(300)
                    _navigationReady.value = true
                    return@launch
                }

                val minSplashTime = async { delay(2000) }

                _loadingText.value = "Loading movie data"
                _loadingProgress.value = 20

                val preloadResult = appPreloadManager.preloadAllData()

                _loadingProgress.value = 60
                _loadingText.value = "Processing data"

                delay(500)

                _loadingProgress.value = 80
                _loadingText.value = "Preparing interface"

                delay(300)

                _loadingProgress.value = 95
                _loadingText.value = "Almost ready"

                minSplashTime.await()

                _loadingProgress.value = 100
                _loadingText.value = "Ready"

                delay(400)

                when (preloadResult) {
                    is Result.Success -> {
                        Log.d(TAG, "Preloading completed successfully")
                    }
                    is Result.Error -> {
                        Log.e(TAG, "Preloading failed but continuing: ${preloadResult.message}")
                    }
                    else -> {}
                }

                _navigationReady.value = true

            } catch (e: Exception) {
                Log.e(TAG, "Error during preloading", e)
                _loadingProgress.value = 100
                _loadingText.value = "Ready!"
                delay(500)
                _navigationReady.value = true
            }
        }
    }
}
