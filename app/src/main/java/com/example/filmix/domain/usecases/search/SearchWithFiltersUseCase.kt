package com.example.filmix.domain.usecases.search

import com.example.filmix.domain.models.MovieItem
import com.example.filmix.domain.models.SearchFilters
import com.example.filmix.domain.repository.SearchRepository
import com.example.filmix.domain.usecases.base.BaseUseCase
import javax.inject.Inject

class SearchWithFiltersUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) : BaseUseCase<SearchWithFiltersUseCase.Params, List<MovieItem>>() {

    data class Params(
        val query: String,
        val page: Int,
        val filters: SearchFilters
    )

    override suspend fun execute(parameters: Params): List<MovieItem> {
        return searchRepository.searchWithFilters(parameters.query, parameters.page, parameters.filters)
    }
}
