package com.example.filmix.domain.usecases.cache

import com.example.filmix.domain.repository.MovieRepository
import com.example.filmix.domain.usecases.base.NoParamsUseCase
import javax.inject.Inject

class ClearOldCacheUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) : NoParamsUseCase<Unit>() {

    override suspend fun execute() {
        movieRepository.clearOldCache()
    }
}
