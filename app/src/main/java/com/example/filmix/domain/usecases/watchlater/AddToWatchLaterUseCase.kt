package com.example.filmix.domain.usecases.watchlater

import com.example.filmix.domain.models.MovieItem
import com.example.filmix.domain.repository.UserListRepository
import com.example.filmix.domain.usecases.base.BaseUseCase
import javax.inject.Inject

class AddToWatchLaterUseCase @Inject constructor(
    private val userListRepository: UserListRepository
) : BaseUseCase<AddToWatchLaterUseCase.Params, Unit>() {

    data class Params(
        val movie: MovieItem
    )

    override suspend fun execute(parameters: Params) {
        userListRepository.addToWatchLater(parameters.movie)
    }
}
