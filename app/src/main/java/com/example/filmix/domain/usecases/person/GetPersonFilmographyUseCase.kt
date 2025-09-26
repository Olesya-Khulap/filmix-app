package com.example.filmix.domain.usecases.person

import com.example.filmix.domain.models.MovieItem
import com.example.filmix.domain.repository.MovieRepository
import com.example.filmix.domain.usecases.base.BaseUseCase
import javax.inject.Inject

class GetPersonFilmographyUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) : BaseUseCase<GetPersonFilmographyUseCase.Params, List<MovieItem>>() {

    data class Params(
        val personId: Int
    )

    override suspend fun execute(parameters: Params): List<MovieItem> {
        return movieRepository.getPersonFilmography(parameters.personId)
    }
}
