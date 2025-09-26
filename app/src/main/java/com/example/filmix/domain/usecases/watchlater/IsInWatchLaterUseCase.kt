package com.example.filmix.domain.usecases.watchlater

import com.example.filmix.domain.repository.UserListRepository
import com.example.filmix.domain.usecases.base.BaseUseCase
import javax.inject.Inject

class IsInWatchLaterUseCase @Inject constructor(
    private val userListRepository: UserListRepository
) : BaseUseCase<IsInWatchLaterUseCase.Params, Boolean>() {

    data class Params(
        val movieId: Int
    )

    override suspend fun execute(parameters: Params): Boolean {
        return userListRepository.isInWatchLater(parameters.movieId)
    }
}
