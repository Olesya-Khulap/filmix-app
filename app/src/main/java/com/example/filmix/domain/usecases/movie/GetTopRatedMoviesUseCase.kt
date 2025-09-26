package com.example.filmix.domain.usecases.movie

import com.example.filmix.domain.models.MovieItem
import com.example.filmix.domain.repository.MovieRepository
import com.example.filmix.domain.usecases.base.BaseUseCase
import javax.inject.Inject

class GetTopRatedMoviesUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) : BaseUseCase<GetTopRatedMoviesUseCase.Params, List<MovieItem>>() {

    data class Params(
        val page: Int = 1,
        val forceRefresh: Boolean = false
    )

    override suspend fun execute(parameters: Params): List<MovieItem> {
        return movieRepository.getTopRatedMovies(parameters.page, parameters.forceRefresh)
    }
}
