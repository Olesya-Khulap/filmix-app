package com.example.filmix.domain.usecases.movie

import com.example.filmix.domain.models.MovieFilters
import com.example.filmix.domain.models.MovieItem
import com.example.filmix.domain.repository.MovieRepository
import com.example.filmix.domain.usecases.base.BaseUseCase
import javax.inject.Inject

class GetFilteredMoviesUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) : BaseUseCase<GetFilteredMoviesUseCase.Params, List<MovieItem>>() {

    data class Params(
        val page: Int = 1,
        val filters: MovieFilters
    )

    override suspend fun execute(parameters: Params): List<MovieItem> {
        return movieRepository.getFilteredMovies(parameters.page, parameters.filters)
    }
}
