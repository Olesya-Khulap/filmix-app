package com.example.filmix.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmix.domain.models.MovieItem
import com.example.filmix.domain.usecases.base.Result
import com.example.filmix.domain.usecases.detail.GetMovieDetailContentUseCase
import com.example.filmix.domain.usecases.favourites.AddToFavoritesUseCase
import com.example.filmix.domain.usecases.favourites.RemoveFromFavoritesUseCase
import com.example.filmix.domain.usecases.watchlater.AddToWatchLaterUseCase
import com.example.filmix.domain.usecases.watchlater.RemoveFromWatchLaterUseCase
import com.example.filmix.domain.usecases.viewed.AddToViewedUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val getMovieDetailContentUseCase: GetMovieDetailContentUseCase,
    private val addToFavoritesUseCase: AddToFavoritesUseCase,
    private val removeFromFavoritesUseCase: RemoveFromFavoritesUseCase,
    private val addToWatchLaterUseCase: AddToWatchLaterUseCase,
    private val removeFromWatchLaterUseCase: RemoveFromWatchLaterUseCase,
    private val addToViewedUseCase: AddToViewedUseCase
) : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    private val _movieContent = MutableLiveData<Result<GetMovieDetailContentUseCase.MovieDetailContent>>()
    val movieContent: LiveData<Result<GetMovieDetailContentUseCase.MovieDetailContent>> = _movieContent

    private val _authRequiredMessage = MutableLiveData<String>()
    val authRequiredMessage: LiveData<String> = _authRequiredMessage

    private var currentMovie: MovieItem? = null
    private var isFavorite = false
    private var isInWatchLater = false
    private var isViewed = false

    fun loadMovieDetails(movie: MovieItem) {
        currentMovie = movie

        viewModelScope.launch {
            getMovieDetailContentUseCase(
                GetMovieDetailContentUseCase.Params(
                    movieId = movie.id,
                    mediaType = movie.mediaType ?: "movie"
                )
            ).collect { result ->
                _movieContent.value = result
                if (result is Result.Success) {
                    isFavorite = result.data.isFavorite
                    isInWatchLater = result.data.isInWatchLater
                    isViewed = result.data.isViewed
                }
            }
        }
    }

    fun toggleFavorite() {
        if (!isUserLoggedIn()) {
            _authRequiredMessage.value = "Please log in to add movies to your favorites"
            return
        }

        currentMovie?.let { movie ->
            viewModelScope.launch {
                if (isFavorite) {
                    removeFromFavoritesUseCase(RemoveFromFavoritesUseCase.Params(movie.id))
                        .collect { result ->
                            if (result is Result.Success) {
                                isFavorite = false
                                loadMovieDetails(movie)
                            }
                        }
                } else {
                    addToFavoritesUseCase(AddToFavoritesUseCase.Params(movie))
                        .collect { result ->
                            if (result is Result.Success) {
                                isFavorite = true
                                loadMovieDetails(movie)
                            }
                        }
                }
            }
        }
    }

    fun toggleWatchLater() {
        if (!isUserLoggedIn()) {
            _authRequiredMessage.value = "Please log in to add movies to your watch later list"
            return
        }

        currentMovie?.let { movie ->
            viewModelScope.launch {
                if (isInWatchLater) {
                    removeFromWatchLaterUseCase(RemoveFromWatchLaterUseCase.Params(movie.id))
                        .collect { result ->
                            if (result is Result.Success) {
                                isInWatchLater = false
                                loadMovieDetails(movie)
                            }
                        }
                } else {
                    addToWatchLaterUseCase(AddToWatchLaterUseCase.Params(movie))
                        .collect { result ->
                            if (result is Result.Success) {
                                isInWatchLater = true
                                loadMovieDetails(movie)
                            }
                        }
                }
            }
        }
    }

    fun toggleViewed() {
        if (!isUserLoggedIn()) {
            _authRequiredMessage.value = "Please log in to mark movies as viewed"
            return
        }

        currentMovie?.let { movie ->
            viewModelScope.launch {
                addToViewedUseCase(AddToViewedUseCase.Params(movie))
                    .collect { result ->
                        if (result is Result.Success) {
                            isViewed = true
                            loadMovieDetails(movie)
                        }
                    }
            }
        }
    }

    private fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    fun clearAuthRequiredMessage() {
        _authRequiredMessage.value = ""
    }
}
