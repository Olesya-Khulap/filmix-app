package com.example.filmix.domain.models

data class Country(
    val iso_3166_1: String,
    val english_name: String,
    val native_name: String? = null
)
