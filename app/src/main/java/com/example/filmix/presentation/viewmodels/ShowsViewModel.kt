package com.example.filmix.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmix.domain.models.Country
import com.example.filmix.domain.models.Genre
import com.example.filmix.domain.models.MovieItem
import com.example.filmix.domain.models.ShowFilters
import com.example.filmix.domain.usecases.base.Result
import com.example.filmix.domain.usecases.country.GetCountriesUseCase
import com.example.filmix.domain.usecases.genre.GetTvGenresUseCase
import com.example.filmix.domain.usecases.tvshow.GetFilteredTvShowsUseCase
import com.example.filmix.domain.usecases.tvshow.GetPopularTvShowsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShowsViewModel @Inject constructor(
    private val getPopularTvShowsUseCase: GetPopularTvShowsUseCase,
    private val getTvGenresUseCase: GetTvGenresUseCase,
    private val getCountriesUseCase: GetCountriesUseCase,
    private val getFilteredTvShowsUseCase: GetFilteredTvShowsUseCase
) : ViewModel() {

    private val _shows = MutableLiveData<Result<List<MovieItem>>>()
    val shows: LiveData<Result<List<MovieItem>>> = _shows

    private val _genres = MutableLiveData<Result<List<Genre>>>()
    val genres: LiveData<Result<List<Genre>>> = _genres

    private val _countries = MutableLiveData<Result<List<Country>>>()
    val countries: LiveData<Result<List<Country>>> = _countries

    private val _isFiltered = MutableLiveData<Boolean>()
    val isFiltered: LiveData<Boolean> = _isFiltered

    private var currentPage = 1
    private var isLoadingMore = false
    private var currentFilters: ShowFilters? = null

    init {
        _isFiltered.value = false
    }

    fun loadShows(forceRefresh: Boolean = false) {
        if (isLoadingMore) return

        viewModelScope.launch {
            if (currentFilters != null) {

                getFilteredTvShowsUseCase(GetFilteredTvShowsUseCase.Params(currentPage, currentFilters!!))
                    .collect { result ->
                        _shows.value = result
                    }
            } else {

                getPopularTvShowsUseCase(GetPopularTvShowsUseCase.Params(currentPage, forceRefresh))
                    .collect { result ->
                        _shows.value = result
                    }
            }
        }
    }

    fun loadGenres() {
        viewModelScope.launch {

            getTvGenresUseCase(GetTvGenresUseCase.Params())
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

    fun applyFilters(filters: ShowFilters) {
        currentFilters = filters
        _isFiltered.value = true
        currentPage = 1
        loadShows()
    }

    fun clearFilters() {
        currentFilters = null
        _isFiltered.value = false
        currentPage = 1
        loadShows()
    }

    fun getCurrentFilters(): ShowFilters? = currentFilters

    fun loadMoreShows() {
        if (isLoadingMore) return

        isLoadingMore = true
        currentPage++

        viewModelScope.launch {
            if (currentFilters != null) {
                getFilteredTvShowsUseCase(GetFilteredTvShowsUseCase.Params(currentPage, currentFilters!!))
                    .collect { result ->
                        when (result) {
                            is Result.Success -> {
                                val currentShows = (_shows.value as? Result.Success)?.data ?: emptyList()
                                _shows.value = Result.Success(currentShows + result.data)
                            }
                            is Result.Error -> {
                                _shows.value = result
                            }
                            is Result.Loading -> {

                            }
                        }
                        isLoadingMore = false
                    }
            } else {

                getPopularTvShowsUseCase(GetPopularTvShowsUseCase.Params(currentPage))
                    .collect { result ->
                        when (result) {
                            is Result.Success -> {
                                val currentShows = (_shows.value as? Result.Success)?.data ?: emptyList()
                                _shows.value = Result.Success(currentShows + result.data)
                            }
                            is Result.Error -> {
                                _shows.value = result
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
