package com.example.filmix.domain.repository

import com.example.filmix.domain.models.MovieItem
import com.example.filmix.domain.models.MovieDetails
import com.example.filmix.domain.models.Genre
import com.example.filmix.domain.models.Country
import com.example.filmix.domain.models.ShowFilters

interface TvShowRepository {
    suspend fun getPopularTvShows(page: Int = 1): List<MovieItem>
    suspend fun getAllShowsPage(page: Int): List<MovieItem>
    suspend fun getFilteredTvShows(page: Int, filters: ShowFilters): List<MovieItem>
    suspend fun getTvDetails(tvId: Int): MovieDetails
    suspend fun getTvGenres(): List<Genre>
    suspend fun getCountries(): List<Country>
    suspend fun getFavoriteTvShows(): List<MovieItem>
    suspend fun addToFavorites(tvShow: MovieItem)
    suspend fun removeFromFavorites(tvId: Int)
    suspend fun isTvShowFavorite(tvId: Int): Boolean
}
