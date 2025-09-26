package com.example.filmix.domain.usecases.viewed

import com.example.filmix.domain.models.MovieItem
import com.example.filmix.domain.repository.UserListRepository
import com.example.filmix.domain.usecases.base.NoParamsUseCase
import javax.inject.Inject

class GetViewedUseCase @Inject constructor(
    private val userListRepository: UserListRepository
) : NoParamsUseCase<List<MovieItem>>() {

    override suspend fun execute(): List<MovieItem> {
        return userListRepository.getViewed()
    }
}
