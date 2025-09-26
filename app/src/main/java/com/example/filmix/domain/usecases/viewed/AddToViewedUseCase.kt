package com.example.filmix.domain.usecases.viewed

import com.example.filmix.domain.models.MovieItem
import com.example.filmix.domain.repository.UserListRepository
import com.example.filmix.domain.usecases.base.BaseUseCase
import javax.inject.Inject

class AddToViewedUseCase @Inject constructor(
    private val userListRepository: UserListRepository
) : BaseUseCase<AddToViewedUseCase.Params, Unit>() {

    data class Params(
        val movie: MovieItem
    )

    override suspend fun execute(parameters: Params) {
        userListRepository.addToViewed(parameters.movie)
    }
}
