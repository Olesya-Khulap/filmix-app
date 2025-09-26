package com.example.filmix.domain.usecases.profile

import com.example.filmix.domain.models.MovieItem
import com.example.filmix.domain.repository.UserListRepository
import com.example.filmix.domain.usecases.base.NoParamsUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class GetUserListsUseCase @Inject constructor(
    private val userListRepository: UserListRepository
) : NoParamsUseCase<GetUserListsUseCase.UserLists>() {

    data class UserLists(
        val favorites: List<MovieItem>,
        val watchLater: List<MovieItem>,
        val viewed: List<MovieItem>,
        val favoritesCount: Int,
        val watchLaterCount: Int,
        val viewedCount: Int
    )

    override suspend fun execute(): UserLists = coroutineScope {
        val favoritesDeferred = async { userListRepository.getFavorites() }
        val watchLaterDeferred = async { userListRepository.getWatchLater() }
        val viewedDeferred = async { userListRepository.getViewed() }
        val favoritesCountDeferred = async { userListRepository.getFavoritesCount() }
        val watchLaterCountDeferred = async { userListRepository.getWatchLaterCount() }
        val viewedCountDeferred = async { userListRepository.getViewedCount() }

        UserLists(
            favorites = favoritesDeferred.await(),
            watchLater = watchLaterDeferred.await(),
            viewed = viewedDeferred.await(),
            favoritesCount = favoritesCountDeferred.await(),
            watchLaterCount = watchLaterCountDeferred.await(),
            viewedCount = viewedCountDeferred.await()
        )
    }
}
