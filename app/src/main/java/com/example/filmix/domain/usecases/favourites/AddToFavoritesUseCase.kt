package com.example.filmix.domain.usecases.favourites

import com.example.filmix.domain.models.MovieItem
import com.example.filmix.domain.repository.UserListRepository
import com.example.filmix.domain.usecases.base.BaseUseCase
import javax.inject.Inject

class AddToFavoritesUseCase @Inject constructor(
    private val userListRepository: UserListRepository
) : BaseUseCase<AddToFavoritesUseCase.Params, Unit>() {

    data class Params(
        val movie: MovieItem
    )

    override suspend fun execute(parameters: Params) {
        userListRepository.addToFavorites(parameters.movie)
    }
}
