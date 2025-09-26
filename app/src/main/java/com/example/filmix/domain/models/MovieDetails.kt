package com.example.filmix.domain.models

data class MovieDetails(
    val id: Int,
    val title: String,
    val year: String,
    val genres: List<Genre>,
    val productionCountries: List<ProductionCountry>,
    val runtime: Int,
    val episodeRunTime: List<Int>,
    val numberOfSeasons: Int,
    val status: String,
    val overview: String
)

data class ProductionCountry(
    val iso31661: String,
    val name: String
)
