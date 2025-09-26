package com.example.filmix.domain.usecases.favourites

import com.example.filmix.domain.repository.UserListRepository
import com.example.filmix.domain.usecases.base.BaseUseCase
import javax.inject.Inject

class RemoveFromFavoritesUseCase @Inject constructor(
    private val userListRepository: UserListRepository
) : BaseUseCase<RemoveFromFavoritesUseCase.Params, Unit>() {

    data class Params(
        val movieId: Int
    )

    override suspend fun execute(parameters: Params) {
        userListRepository.removeFromFavorites(parameters.movieId)
    }
}
