package com.example.filmix.domain.usecases.search

import com.example.filmix.domain.models.Genre
import com.example.filmix.domain.repository.SearchRepository
import com.example.filmix.domain.usecases.base.BaseUseCase
import javax.inject.Inject

class GetAllGenresUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) : BaseUseCase<GetAllGenresUseCase.Params, List<Genre>>() {

    class Params

    override suspend fun execute(parameters: Params): List<Genre> {
        return searchRepository.getAllGenres()
    }
}
