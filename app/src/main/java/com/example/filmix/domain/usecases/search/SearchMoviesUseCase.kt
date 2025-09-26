package com.example.filmix.domain.usecases.search

import com.example.filmix.domain.models.MovieItem
import com.example.filmix.domain.repository.SearchRepository
import com.example.filmix.domain.usecases.base.BaseUseCase
import javax.inject.Inject

class SearchMoviesUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) : BaseUseCase<SearchMoviesUseCase.Params, List<MovieItem>>() {

    data class Params(
        val query: String,
        val page: Int = 1
    )

    override suspend fun execute(parameters: Params): List<MovieItem> {
        return if (parameters.query.isEmpty() || parameters.query == "popular") {
            searchRepository.getAllContent(parameters.page)
        } else {
            searchRepository.searchMulti(parameters.query, parameters.page)
        }
    }
}
