package com.example.filmix.domain.usecases.movie

import com.example.filmix.domain.models.MovieItem
import com.example.filmix.domain.repository.MovieRepository
import com.example.filmix.domain.usecases.base.BaseUseCase
import javax.inject.Inject

class GetBannerMoviesUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) : BaseUseCase<GetBannerMoviesUseCase.Params, List<MovieItem>>() {

    data class Params(
        val forceRefresh: Boolean = false
    )

    override suspend fun execute(parameters: Params): List<MovieItem> {
        return movieRepository.getBannerMovies(parameters.forceRefresh)
    }
}
