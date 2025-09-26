package com.example.filmix.domain.usecases.home

import com.example.filmix.domain.models.MovieItem
import com.example.filmix.domain.repository.MovieRepository
import com.example.filmix.domain.usecases.base.BaseUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class GetHomeContentUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) : BaseUseCase<GetHomeContentUseCase.Params, GetHomeContentUseCase.HomeContent>() {

    data class Params(
        val forceRefresh: Boolean = false
    )

    data class HomeContent(
        val bannerMovies: List<MovieItem>,
        val popularMovies: List<MovieItem>,
        val popularTvShows: List<MovieItem>,
        val newReleases: List<MovieItem>,
        val topRated: List<MovieItem>
    )

    override suspend fun execute(parameters: Params): HomeContent = coroutineScope {
        val bannerDeferred = async { movieRepository.getBannerMovies(parameters.forceRefresh) }
        val popularMoviesDeferred = async { movieRepository.getPopularMovies(1, parameters.forceRefresh) }
        val popularTvDeferred = async { movieRepository.getPopularTvShows(1, parameters.forceRefresh) }
        val newReleasesDeferred = async { movieRepository.getNowPlayingMovies(1, parameters.forceRefresh) }
        val topRatedDeferred = async { movieRepository.getTopRatedMovies(1, parameters.forceRefresh) }

        HomeContent(
            bannerMovies = bannerDeferred.await(),
            popularMovies = popularMoviesDeferred.await(),
            popularTvShows = popularTvDeferred.await(),
            newReleases = newReleasesDeferred.await(),
            topRated = topRatedDeferred.await()
        )
    }
}
