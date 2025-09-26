package com.example.filmix.data.repository

import com.example.filmix.data.remote.TmdbApiService
import com.example.filmix.domain.models.*
import com.example.filmix.domain.repository.TvShowRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TvShowRepositoryImpl @Inject constructor(
    private val apiService: TmdbApiService
) : TvShowRepository {

    override suspend fun getPopularTvShows(page: Int): List<MovieItem> {
        return try {
            apiService.getPopularTvShows(page)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun getAllShowsPage(page: Int): List<MovieItem> {
        return try {
            apiService.getPopularTvShows(page)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun getFilteredTvShows(page: Int, filters: ShowFilters): List<MovieItem> {
        return try {
            apiService.getFilteredTvShows(page, filters)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun getTvDetails(tvId: Int): MovieDetails {
        return try {
            apiService.getTvDetails(tvId)
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    override suspend fun getTvGenres(): List<Genre> {
        return try {
            apiService.getTvGenres()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun getCountries(): List<Country> {
        return try {
            apiService.getCountries()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun getFavoriteTvShows(): List<MovieItem> {
        return emptyList()
    }

    override suspend fun addToFavorites(tvShow: MovieItem) {
    }

    override suspend fun removeFromFavorites(tvId: Int) {
    }

    override suspend fun isTvShowFavorite(tvId: Int): Boolean {
        return false
    }
}
