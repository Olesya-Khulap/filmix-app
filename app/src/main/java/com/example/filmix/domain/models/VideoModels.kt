package com.example.filmix.domain.models

data class VideoResponse(
    val results: List<VideoResult>
)

data class VideoResult(
    val id: String = "",
    val iso_639_1: String = "",
    val iso_3166_1: String = "",
    val key: String,
    val name: String,
    val official: Boolean = false,
    val published_at: String = "",
    val site: String,
    val size: Int = 0,
    val type: String
)
