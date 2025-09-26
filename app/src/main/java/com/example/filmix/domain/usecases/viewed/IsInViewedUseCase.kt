package com.example.filmix.domain.usecases.viewed

import com.example.filmix.domain.repository.UserListRepository
import com.example.filmix.domain.usecases.base.BaseUseCase
import javax.inject.Inject

class IsInViewedUseCase @Inject constructor(
    private val userListRepository: UserListRepository
) : BaseUseCase<IsInViewedUseCase.Params, Boolean>() {

    data class Params(val movieId: Int)

    override suspend fun execute(params: Params): Boolean {
        return userListRepository.isViewed(params.movieId)
    }
}
