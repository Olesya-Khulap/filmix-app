package com.example.filmix.domain.repository

import com.example.filmix.domain.models.Country
import com.example.filmix.domain.models.Genre
import com.example.filmix.domain.models.MovieItem
import com.example.filmix.domain.models.SearchFilters

interface SearchRepository {
    suspend fun searchMovies(query: String, page: Int): List<MovieItem>
    suspend fun searchTvShows(query: String, page: Int): List<MovieItem>
    suspend fun searchMulti(query: String, page: Int): List<MovieItem>
    suspend fun searchWithFilters(query: String, page: Int, filters: SearchFilters): List<MovieItem>
    suspend fun getAllMovies(page: Int): List<MovieItem>
    suspend fun getAllTvShows(page: Int): List<MovieItem>
    suspend fun getAllContent(page: Int): List<MovieItem>
    suspend fun getAllGenres(): List<Genre>
    suspend fun getCountries(): List<Country>
}
