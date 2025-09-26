package com.example.filmix.domain.usecases.favourites

import com.example.filmix.domain.repository.UserListRepository
import com.example.filmix.domain.usecases.base.BaseUseCase
import javax.inject.Inject

class IsFavoriteUseCase @Inject constructor(
    private val userListRepository: UserListRepository
) : BaseUseCase<IsFavoriteUseCase.Params, Boolean>() {

    data class Params(
        val movieId: Int
    )

    override suspend fun execute(parameters: Params): Boolean {
        return userListRepository.isFavorite(parameters.movieId)
    }
}
