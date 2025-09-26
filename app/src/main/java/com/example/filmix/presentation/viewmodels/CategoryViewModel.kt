package com.example.filmix.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmix.domain.models.CategoryType
import com.example.filmix.domain.models.MovieItem
import com.example.filmix.domain.usecases.base.Result
import com.example.filmix.domain.usecases.movie.GetPopularMoviesUseCase
import com.example.filmix.domain.usecases.movie.GetTopRatedMoviesUseCase
import com.example.filmix.domain.usecases.movie.GetNowPlayingMoviesUseCase
import com.example.filmix.domain.usecases.tvshow.GetPopularTvShowsUseCase
import com.example.filmix.domain.usecases.profile.GetUserListsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    private val getPopularTvShowsUseCase: GetPopularTvShowsUseCase,
    private val getTopRatedMoviesUseCase: GetTopRatedMoviesUseCase,
    private val getNowPlayingMoviesUseCase: GetNowPlayingMoviesUseCase,
    private val getUserListsUseCase: GetUserListsUseCase
) : ViewModel() {

    private val _movies = MutableLiveData<Result<List<MovieItem>>>()
    val movies: LiveData<Result<List<MovieItem>>> = _movies

    fun loadMoviesByCategory(categoryType: CategoryType) {
        viewModelScope.launch {
            _movies.value = Result.Loading()

            when (categoryType) {
                CategoryType.POPULARMOVIES -> {
                    getPopularMoviesUseCase(GetPopularMoviesUseCase.Params(page = 1))
                        .collect { result ->
                            _movies.value = result
                        }
                }
                CategoryType.POPULARSERIES -> {
                    getPopularTvShowsUseCase(GetPopularTvShowsUseCase.Params(page = 1))
                        .collect { result ->
                            _movies.value = result
                        }
                }
                CategoryType.NEWRELEASES -> {
                    getNowPlayingMoviesUseCase(GetNowPlayingMoviesUseCase.Params(page = 1))
                        .collect { result ->
                            _movies.value = result
                        }
                }
                CategoryType.TOPRATED -> {
                    getTopRatedMoviesUseCase(GetTopRatedMoviesUseCase.Params(page = 1))
                        .collect { result ->
                            _movies.value = result
                        }
                }

                CategoryType.CUSTOM_FAVOURITES -> {
                    getUserListsUseCase()
                        .collect { result ->
                            when (result) {
                                is Result.Success -> {
                                    _movies.value = Result.Success(result.data.favorites)
                                }
                                is Result.Error -> {
                                    _movies.value = Result.Error(result.message)
                                }
                                is Result.Loading -> {
                                    _movies.value = Result.Loading()
                                }
                            }
                        }
                }
                CategoryType.CUSTOM_WATCH_LATER -> {
                    getUserListsUseCase()
                        .collect { result ->
                            when (result) {
                                is Result.Success -> {
                                    _movies.value = Result.Success(result.data.watchLater)
                                }
                                is Result.Error -> {
                                    _movies.value = Result.Error(result.message)
                                }
                                is Result.Loading -> {
                                    _movies.value = Result.Loading()
                                }
                            }
                        }
                }
                CategoryType.CUSTOM_VIEWED -> {
                    getUserListsUseCase()
                        .collect { result ->
                            when (result) {
                                is Result.Success -> {
                                    _movies.value = Result.Success(result.data.viewed)
                                }
                                is Result.Error -> {
                                    _movies.value = Result.Error(result.message)
                                }
                                is Result.Loading -> {
                                    _movies.value = Result.Loading()
                                }
                            }
                        }
                }
            }
        }
    }
}
