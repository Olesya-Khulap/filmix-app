package com.example.filmix.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmix.domain.models.Country
import com.example.filmix.domain.models.Genre
import com.example.filmix.domain.models.MovieItem
import com.example.filmix.domain.models.SearchFilters
import com.example.filmix.domain.usecases.base.Result
import com.example.filmix.domain.usecases.country.GetCountriesUseCase
import com.example.filmix.domain.usecases.search.GetAllGenresUseCase
import com.example.filmix.domain.usecases.search.SearchWithFiltersUseCase
import com.example.filmix.domain.usecases.search.SearchMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchMoviesUseCase: SearchMoviesUseCase,
    private val searchWithFiltersUseCase: SearchWithFiltersUseCase,
    private val getAllGenresUseCase: GetAllGenresUseCase,
    private val getCountriesUseCase: GetCountriesUseCase
) : ViewModel() {

    private val _searchResults = MutableLiveData<Result<List<MovieItem>>>()
    val searchResults: LiveData<Result<List<MovieItem>>> = _searchResults

    private val _genres = MutableLiveData<Result<List<Genre>>>()
    val genres: LiveData<Result<List<Genre>>> = _genres

    private val _countries = MutableLiveData<Result<List<Country>>>()
    val countries: LiveData<Result<List<Country>>> = _countries

    private val _isFiltered = MutableLiveData<Boolean>()
    val isFiltered: LiveData<Boolean> = _isFiltered

    private var currentPage = 1
    private var isLoadingMore = false
    private var currentQuery = ""
    private var currentFilters: SearchFilters? = null

    init {
        _isFiltered.value = false
    }

    fun searchMovies(query: String) {
        currentQuery = query
        currentPage = 1
        performSearch()
    }

    fun loadAllMovies() {
        currentQuery = ""
        currentPage = 1
        performSearch()
    }

    private fun performSearch() {
        if (isLoadingMore) return

        viewModelScope.launch {
            if (currentFilters != null) {
                searchWithFiltersUseCase(SearchWithFiltersUseCase.Params(currentQuery, currentPage, currentFilters!!))
                    .collect { result ->
                        _searchResults.value = result
                    }
            } else {
                if (currentQuery.isNotEmpty()) {
                    searchMoviesUseCase(SearchMoviesUseCase.Params(currentQuery, currentPage))
                        .collect { result ->
                            _searchResults.value = result
                        }
                } else {
                    searchMoviesUseCase(SearchMoviesUseCase.Params("", currentPage))
                        .collect { result ->
                            _searchResults.value = result
                        }
                }
            }
        }
    }

    fun loadGenres() {
        viewModelScope.launch {
            getAllGenresUseCase(GetAllGenresUseCase.Params())
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

    fun applyFilters(filters: SearchFilters) {
        currentFilters = filters
        _isFiltered.value = true
        currentPage = 1
        performSearch()
    }

    fun clearFilters() {
        currentFilters = null
        _isFiltered.value = false
        currentPage = 1
        performSearch()
    }

    fun getCurrentFilters(): SearchFilters? = currentFilters

    fun loadMoreResults() {
        if (isLoadingMore) return

        isLoadingMore = true
        currentPage++

        viewModelScope.launch {
            if (currentFilters != null) {
                searchWithFiltersUseCase(SearchWithFiltersUseCase.Params(currentQuery, currentPage, currentFilters!!))
                    .collect { result ->
                        when (result) {
                            is Result.Success -> {
                                val currentResults = (_searchResults.value as? Result.Success)?.data ?: emptyList()
                                _searchResults.value = Result.Success(currentResults + result.data)
                            }
                            is Result.Error -> {
                                _searchResults.value = result
                            }
                            is Result.Loading -> {
                                // Не показываем loading при loadMore
                            }
                        }
                        isLoadingMore = false
                    }
            } else {
                searchMoviesUseCase(SearchMoviesUseCase.Params(currentQuery.ifEmpty { "" }, currentPage))
                    .collect { result ->
                        when (result) {
                            is Result.Success -> {
                                val currentResults = (_searchResults.value as? Result.Success)?.data ?: emptyList()
                                _searchResults.value = Result.Success(currentResults + result.data)
                            }
                            is Result.Error -> {
                                _searchResults.value = result
                            }
                            is Result.Loading -> {
                                // Не показываем loading при loadMore
                            }
                        }
                        isLoadingMore = false
                    }
            }
        }
    }
}
