package com.example.filmix.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieItem(
    val id: Int,
    val title: String,
    val year: String,
    val posterPath: String,
    val rating: Double,
    val backdropPath: String? = null,
    val mediaType: String? = null,
    val popularity: Double = 0.0
) : Parcelable
