package com.example.filmix.domain.usecases.tvshow

import com.example.filmix.domain.models.MovieItem
import com.example.filmix.domain.models.ShowFilters
import com.example.filmix.domain.repository.TvShowRepository
import com.example.filmix.domain.usecases.base.BaseUseCase
import javax.inject.Inject

class GetFilteredTvShowsUseCase @Inject constructor(
    private val tvShowRepository: TvShowRepository
) : BaseUseCase<GetFilteredTvShowsUseCase.Params, List<MovieItem>>() {

    data class Params(
        val page: Int,
        val filters: ShowFilters
    )

    override suspend fun execute(parameters: Params): List<MovieItem> {
        return tvShowRepository.getFilteredTvShows(parameters.page, parameters.filters)
    }
}
