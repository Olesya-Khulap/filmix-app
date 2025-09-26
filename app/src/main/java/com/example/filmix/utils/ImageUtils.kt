package com.example.filmix.utils

object ImageUtils {

    fun getPosterUrl(posterPath: String?): String {
        return if (posterPath.isNullOrEmpty()) {
            ""
        } else {
            "${Constants.BASE_IMAGE_URL}${Constants.POSTER_SIZE_W500}$posterPath"
        }
    }

    fun getBackdropUrl(backdropPath: String?): String {
        return if (backdropPath.isNullOrEmpty()) {
            ""
        } else {
            "${Constants.BASE_IMAGE_URL}${Constants.BACKDROP_SIZE_W1280}$backdropPath"
        }
    }

    fun getProfileUrl(profilePath: String?): String {
        return if (profilePath.isNullOrEmpty()) {
            ""
        } else {
            "${Constants.BASE_IMAGE_URL}${Constants.PROFILE_SIZE_W185}$profilePath"
        }
    }
}
