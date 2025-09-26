package com.example.filmix.domain.usecases.tvshow

import com.example.filmix.domain.models.MovieItem
import com.example.filmix.domain.repository.MovieRepository
import com.example.filmix.domain.usecases.base.BaseUseCase
import javax.inject.Inject

class GetPopularTvShowsUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) : BaseUseCase<GetPopularTvShowsUseCase.Params, List<MovieItem>>() {

    data class Params(
        val page: Int = 1,
        val forceRefresh: Boolean = false
    )

    override suspend fun execute(parameters: Params): List<MovieItem> {
        return movieRepository.getPopularTvShows(parameters.page, parameters.forceRefresh)
    }
}
