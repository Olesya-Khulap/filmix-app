package com.example.filmix.domain.usecases.genre

import com.example.filmix.domain.models.Genre
import com.example.filmix.domain.repository.MovieRepository
import com.example.filmix.domain.usecases.base.BaseUseCase
import javax.inject.Inject

class GetTvGenresUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) : BaseUseCase<GetTvGenresUseCase.Params, List<Genre>>() {

    data class Params(
        val forceRefresh: Boolean = false
    )

    override suspend fun execute(parameters: Params): List<Genre> {
        return movieRepository.getTvGenres(parameters.forceRefresh)
    }
}
