package com.example.filmix.domain.usecases.media

import com.example.filmix.domain.models.ImageResponse
import com.example.filmix.domain.repository.MovieRepository
import com.example.filmix.domain.usecases.base.BaseUseCase
import javax.inject.Inject

class GetMovieImagesUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) : BaseUseCase<GetMovieImagesUseCase.Params, ImageResponse>() {

    data class Params(
        val movieId: Int,
        val forceRefresh: Boolean = false
    )

    override suspend fun execute(parameters: Params): ImageResponse {
        return movieRepository.getMovieImages(parameters.movieId, parameters.forceRefresh)
    }
}
