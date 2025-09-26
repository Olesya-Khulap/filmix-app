package com.example.filmix.domain.usecases.country

import com.example.filmix.domain.models.Country
import com.example.filmix.domain.repository.MovieRepository
import com.example.filmix.domain.usecases.base.BaseUseCase
import javax.inject.Inject

class GetCountriesUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) : BaseUseCase<GetCountriesUseCase.Params, List<Country>>() {

    data class Params(val dummy: String = "")

    override suspend fun execute(parameters: Params): List<Country> {
        return movieRepository.getCountries()
    }
}
