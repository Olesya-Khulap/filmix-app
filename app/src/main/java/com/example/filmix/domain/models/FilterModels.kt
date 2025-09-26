package com.example.filmix.domain.models

data class MovieFilters(
    val genreIds: List<Int> = emptyList(),
    val countryIds: List<String> = emptyList(),
    val minRating: Double? = null,
    val maxRating: Double? = null,
    val yearFrom: Int? = null,
    val yearTo: Int? = null
)

data class ShowFilters(
    val genreIds: List<Int> = emptyList(),
    val countryIds: List<String> = emptyList(),
    val minRating: Double? = null,
    val maxRating: Double? = null,
    val yearFrom: Int? = null,
    val yearTo: Int? = null,
    val statuses: List<String> = emptyList()
)

data class SearchFilters(
    val types: List<String> = emptyList(),
    val genreIds: List<Int> = emptyList(),
    val countryIds: List<String> = emptyList(),
    val minRating: Double? = null,
    val maxRating: Double? = null,
    val yearFrom: Int? = null,
    val yearTo: Int? = null,
    val statuses: List<String> = emptyList()
)

