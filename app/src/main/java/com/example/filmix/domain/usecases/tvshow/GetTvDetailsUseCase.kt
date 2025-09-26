package com.example.filmix.domain.usecases.tvshow

import com.example.filmix.domain.models.MovieDetails
import com.example.filmix.domain.repository.MovieRepository
import com.example.filmix.domain.usecases.base.BaseUseCase
import javax.inject.Inject

class GetTvDetailsUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) : BaseUseCase<GetTvDetailsUseCase.Params, MovieDetails>() {

    data class Params(
        val tvId: Int,
        val forceRefresh: Boolean = false
    )

    override suspend fun execute(parameters: Params): MovieDetails {
        return movieRepository.getTvDetails(parameters.tvId, parameters.forceRefresh)
    }
}
