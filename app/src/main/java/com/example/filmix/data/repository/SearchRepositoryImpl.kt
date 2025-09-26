package com.example.filmix.data.repository

import com.example.filmix.data.remote.TmdbApiService
import com.example.filmix.domain.models.*
import com.example.filmix.domain.repository.SearchRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepositoryImpl @Inject constructor(
    private val apiService: TmdbApiService
) : SearchRepository {

    override suspend fun searchMovies(query: String, page: Int): List<MovieItem> {
        return try {
            apiService.searchMovies(query, page)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun searchTvShows(query: String, page: Int): List<MovieItem> {
        return try {
            apiService.searchTvShows(query, page)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun searchMulti(query: String, page: Int): List<MovieItem> {
        return try {
            apiService.searchMulti(query, page)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun searchWithFilters(query: String, page: Int, filters: SearchFilters): List<MovieItem> {
        return try {
            apiService.searchWithFilters(query, page, filters)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun getAllMovies(page: Int): List<MovieItem> {
        return try {
            apiService.getPopularMovies(page)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun getAllTvShows(page: Int): List<MovieItem> {
        return try {
            apiService.getPopularTvShows(page)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun getAllContent(page: Int): List<MovieItem> {
        return try {
            val movies = apiService.getPopularMovies(page)
            val shows = apiService.getPopularTvShows(page)
            (movies + shows).shuffled()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun getAllGenres(): List<Genre> {
        return try {
            val movieGenres = apiService.getMovieGenres()
            val tvGenres = apiService.getTvGenres()
            (movieGenres + tvGenres).distinctBy { it.id }
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
}
