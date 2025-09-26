package com.example.filmix.domain.usecases.person

import com.example.filmix.domain.models.PersonDetails
import com.example.filmix.domain.repository.MovieRepository
import com.example.filmix.domain.usecases.base.BaseUseCase
import javax.inject.Inject

class GetPersonDetailsUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) : BaseUseCase<GetPersonDetailsUseCase.Params, PersonDetails>() {

    data class Params(
        val personId: Int
    )

    override suspend fun execute(parameters: Params): PersonDetails {
        return movieRepository.getPersonDetails(parameters.personId)
    }
}
