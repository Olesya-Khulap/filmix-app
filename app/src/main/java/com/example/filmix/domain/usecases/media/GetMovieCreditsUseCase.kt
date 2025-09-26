package com.example.filmix.domain.usecases.media

import com.example.filmix.domain.models.CreditsResponse
import com.example.filmix.domain.repository.MovieRepository
import com.example.filmix.domain.usecases.base.BaseUseCase
import javax.inject.Inject

class GetMovieCreditsUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) : BaseUseCase<GetMovieCreditsUseCase.Params, CreditsResponse>() {

    data class Params(
        val movieId: Int,
        val forceRefresh: Boolean = false
    )

    override suspend fun execute(parameters: Params): CreditsResponse {
        return movieRepository.getMovieCredits(parameters.movieId, parameters.forceRefresh)
    }
}
