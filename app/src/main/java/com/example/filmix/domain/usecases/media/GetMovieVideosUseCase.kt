package com.example.filmix.domain.usecases.media

import com.example.filmix.domain.models.VideoResponse
import com.example.filmix.domain.repository.MovieRepository
import com.example.filmix.domain.usecases.base.BaseUseCase
import javax.inject.Inject

class GetMovieVideosUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) : BaseUseCase<GetMovieVideosUseCase.Params, VideoResponse>() {

    data class Params(
        val movieId: Int,
        val forceRefresh: Boolean = false
    )

    override suspend fun execute(parameters: Params): VideoResponse {
        return movieRepository.getMovieVideos(parameters.movieId, parameters.forceRefresh)
    }
}
