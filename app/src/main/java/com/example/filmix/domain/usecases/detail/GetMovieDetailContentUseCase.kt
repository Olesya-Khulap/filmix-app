package com.example.filmix.domain.usecases.detail

import com.example.filmix.domain.models.*
import com.example.filmix.domain.repository.MovieRepository
import com.example.filmix.domain.repository.UserListRepository
import com.example.filmix.domain.usecases.base.BaseUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class GetMovieDetailContentUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
    private val userListRepository: UserListRepository
) : BaseUseCase<GetMovieDetailContentUseCase.Params, GetMovieDetailContentUseCase.MovieDetailContent>() {

    data class Params(
        val movieId: Int,
        val mediaType: String = "movie",
        val forceRefresh: Boolean = false
    )

    data class MovieDetailContent(
        val details: MovieDetails,
        val images: ImageResponse,
        val videos: VideoResponse,
        val credits: CreditsResponse,
        val isFavorite: Boolean,
        val isInWatchLater: Boolean,
        val isViewed: Boolean
    )

    override suspend fun execute(parameters: Params): MovieDetailContent = coroutineScope {
        val detailsDeferred = async {
            if (parameters.mediaType == "tv") {
                movieRepository.getTvDetails(parameters.movieId, parameters.forceRefresh)
            } else {
                movieRepository.getMovieDetails(parameters.movieId, parameters.forceRefresh)
            }
        }

        val imagesDeferred = async {
            if (parameters.mediaType == "tv") {
                movieRepository.getTvImages(parameters.movieId, parameters.forceRefresh)
            } else {
                movieRepository.getMovieImages(parameters.movieId, parameters.forceRefresh)
            }
        }

        val videosDeferred = async {
            if (parameters.mediaType == "tv") {
                movieRepository.getTvVideos(parameters.movieId, parameters.forceRefresh)
            } else {
                movieRepository.getMovieVideos(parameters.movieId, parameters.forceRefresh)
            }
        }

        val creditsDeferred = async {
            if (parameters.mediaType == "tv") {
                movieRepository.getTvCredits(parameters.movieId, parameters.forceRefresh)
            } else {
                movieRepository.getMovieCredits(parameters.movieId, parameters.forceRefresh)
            }
        }

        val isFavoriteDeferred = async { userListRepository.isFavorite(parameters.movieId) }
        val isInWatchLaterDeferred = async { userListRepository.isInWatchLater(parameters.movieId) }
        val isViewedDeferred = async { userListRepository.isViewed(parameters.movieId) }

        MovieDetailContent(
            details = detailsDeferred.await(),
            images = imagesDeferred.await(),
            videos = videosDeferred.await(),
            credits = creditsDeferred.await(),
            isFavorite = isFavoriteDeferred.await(),
            isInWatchLater = isInWatchLaterDeferred.await(),
            isViewed = isViewedDeferred.await()
        )
    }
}
