package com.example.filmix.domain.models

data class PersonDetails(
    val id: Int,
    val name: String,
    val biography: String?,
    val birthday: String?,
    val placeOfBirth: String?,
    val profilePath: String?
)
