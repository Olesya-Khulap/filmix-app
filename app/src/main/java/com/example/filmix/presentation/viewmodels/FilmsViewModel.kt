package com.example.filmix.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmix.domain.models.Country
import com.example.filmix.domain.models.Genre
import com.example.filmix.domain.models.MovieFilters
import com.example.filmix.domain.models.MovieItem
import com.example.filmix.domain.usecases.base.Result
import com.example.filmix.domain.usecases.country.GetCountriesUseCase
import com.example.filmix.domain.usecases.genre.GetMovieGenresUseCase
import com.example.filmix.domain.usecases.movie.GetFilteredMoviesUseCase
import com.example.filmix.domain.usecases.movie.GetPopularMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilmsViewModel @Inject constructor(
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    private val getMovieGenresUseCase: GetMovieGenresUseCase,
    private val getCountriesUseCase: GetCountriesUseCase,
    private val getFilteredMoviesUseCase: GetFilteredMoviesUseCase
) : ViewModel() {

    private val _movies = MutableLiveData<Result<List<MovieItem>>>()
    val movies: LiveData<Result<List<MovieItem>>> = _movies

    private val _genres = MutableLiveData<Result<List<Genre>>>()
    val genres: LiveData<Result<List<Genre>>> = _genres

    private val _countries = MutableLiveData<Result<List<Country>>>()
    val countries: LiveData<Result<List<Country>>> = _countries

    private val _isFiltered = MutableLiveData<Boolean>()
    val isFiltered: LiveData<Boolean> = _isFiltered

    private var currentPage = 1
    private var isLoadingMore = false
    private var currentFilters: MovieFilters? = null

    init {
        _isFiltered.value = false
    }

    fun loadMovies(forceRefresh: Boolean = false) {
        if (isLoadingMore) return

        viewModelScope.launch {
            if (currentFilters != null) {

                getFilteredMoviesUseCase(GetFilteredMoviesUseCase.Params(currentPage, currentFilters!!))
                    .collect { result ->
                        _movies.value = result
                    }
            } else {

                getPopularMoviesUseCase(GetPopularMoviesUseCase.Params(currentPage, forceRefresh))
                    .collect { result ->
                        _movies.value = result
                    }
            }
        }
    }

    fun loadGenres() {
        viewModelScope.launch {

            getMovieGenresUseCase(GetMovieGenresUseCase.Params())
                .collect { result ->
                    _genres.value = result
                }
        }
    }

    fun loadCountries() {
        viewModelScope.launch {
            getCountriesUseCase(GetCountriesUseCase.Params())
                .collect { result ->
                    _countries.value = result
                }
        }
    }

    fun applyFilters(filters: MovieFilters) {
        currentFilters = filters
        _isFiltered.value = true
        currentPage = 1
        loadMovies()
    }

    fun clearFilters() {
        currentFilters = null
        _isFiltered.value = false
        currentPage = 1
        loadMovies()
    }

    fun getCurrentFilters(): MovieFilters? = currentFilters

    fun loadMoreMovies() {
        if (isLoadingMore) return

        isLoadingMore = true
        currentPage++

        viewModelScope.launch {
            if (currentFilters != null) {

                getFilteredMoviesUseCase(GetFilteredMoviesUseCase.Params(currentPage, currentFilters!!))
                    .collect { result ->
                        when (result) {
                            is Result.Success -> {
                                val currentMovies = (_movies.value as? Result.Success)?.data ?: emptyList()
                                _movies.value = Result.Success(currentMovies + result.data)
                            }
                            is Result.Error -> {
                                _movies.value = result
                            }
                            is Result.Loading -> {

                            }
                        }
                        isLoadingMore = false
                    }
            } else {

                getPopularMoviesUseCase(GetPopularMoviesUseCase.Params(currentPage))
                    .collect { result ->
                        when (result) {
                            is Result.Success -> {
                                val currentMovies = (_movies.value as? Result.Success)?.data ?: emptyList()
                                _movies.value = Result.Success(currentMovies + result.data)
                            }
                            is Result.Error -> {
                                _movies.value = result
                            }
                            is Result.Loading -> {
                            }
                        }
                        isLoadingMore = false
                    }
            }
        }
    }
}
