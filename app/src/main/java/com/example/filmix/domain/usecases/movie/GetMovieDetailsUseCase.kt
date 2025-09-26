package com.example.filmix.domain.usecases.movie

import com.example.filmix.domain.models.MovieDetails
import com.example.filmix.domain.repository.MovieRepository
import com.example.filmix.domain.usecases.base.BaseUseCase
import javax.inject.Inject

class GetMovieDetailsUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) : BaseUseCase<GetMovieDetailsUseCase.Params, MovieDetails>() {

    data class Params(
        val movieId: Int,
        val forceRefresh: Boolean = false
    )

    override suspend fun execute(parameters: Params): MovieDetails {
        return movieRepository.getMovieDetails(parameters.movieId, parameters.forceRefresh)
    }
}
