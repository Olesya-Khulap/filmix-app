package com.example.filmix.domain.models

enum class CategoryType(val title: String) {
    POPULARMOVIES("Popular Movies"),
    POPULARSERIES("Popular Series"),
    NEWRELEASES("New Releases"),
    TOPRATED("Top Rated"),

    CUSTOM_FAVOURITES("Favourites"),
    CUSTOM_WATCH_LATER("Watch Later"),
    CUSTOM_VIEWED("Viewed")
}
