package com.example.filmix.domain.usecases.viewed

import com.example.filmix.domain.repository.UserListRepository
import com.example.filmix.domain.usecases.base.BaseUseCase
import javax.inject.Inject

class RemoveFromViewedUseCase @Inject constructor(
    private val userListRepository: UserListRepository
) : BaseUseCase<RemoveFromViewedUseCase.Params, Unit>() {

    data class Params(val movieId: Int)

    override suspend fun execute(params: Params) {
        userListRepository.removeFromViewed(params.movieId)
    }
}
