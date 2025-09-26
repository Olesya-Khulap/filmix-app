package com.example.filmix.domain.models

data class ImageResponse(
    val backdrops: List<ImageInfo>,
    val posters: List<ImageInfo>
)

data class ImageInfo(
    val filePath: String,
    val aspectRatio: Double = 0.0,
    val height: Int = 0,
    val width: Int = 0,
    val voteAverage: Double = 0.0,
    val voteCount: Int = 0
)
