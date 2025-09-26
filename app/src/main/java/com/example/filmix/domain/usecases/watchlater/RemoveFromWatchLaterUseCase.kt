package com.example.filmix.domain.usecases.watchlater

import com.example.filmix.domain.repository.UserListRepository
import com.example.filmix.domain.usecases.base.BaseUseCase
import javax.inject.Inject

class RemoveFromWatchLaterUseCase @Inject constructor(
    private val userListRepository: UserListRepository
) : BaseUseCase<RemoveFromWatchLaterUseCase.Params, Unit>() {

    data class Params(
        val movieId: Int
    )

    override suspend fun execute(parameters: Params) {
        userListRepository.removeFromWatchLater(parameters.movieId)
    }
}
