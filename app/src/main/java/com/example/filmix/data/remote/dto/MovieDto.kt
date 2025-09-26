package com.example.filmix.data.remote.dto

import com.google.gson.annotations.SerializedName

data class MovieListResponse(
    @SerializedName("page") val page: Int,
    @SerializedName("results") val results: List<MovieDto>,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("total_results") val totalResults: Int
)

data class MovieDto(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("original_title") val originalTitle: String?,
    @SerializedName("overview") val overview: String?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("backdrop_path") val backdropPath: String?,
    @SerializedName("release_date") val releaseDate: String?,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("vote_count") val voteCount: Int,
    @SerializedName("popularity") val popularity: Double,
    @SerializedName("adult") val adult: Boolean,
    @SerializedName("genre_ids") val genreIds: List<Int>?,
    @SerializedName("original_language") val originalLanguage: String?,
    @SerializedName("video") val video: Boolean
)

data class MovieDetailsDto(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("original_title") val originalTitle: String?,
    @SerializedName("overview") val overview: String?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("backdrop_path") val backdropPath: String?,
    @SerializedName("release_date") val releaseDate: String?,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("vote_count") val voteCount: Int,
    @SerializedName("popularity") val popularity: Double,
    @SerializedName("runtime") val runtime: Int?,
    @SerializedName("status") val status: String?,
    @SerializedName("genres") val genres: List<GenreDto>,
    @SerializedName("production_countries") val productionCountries: List<ProductionCountryDto>,
    @SerializedName("production_companies") val productionCompanies: List<ProductionCompanyDto>?,
    @SerializedName("spoken_languages") val spokenLanguages: List<SpokenLanguageDto>?,
    @SerializedName("budget") val budget: Long?,
    @SerializedName("revenue") val revenue: Long?,
    @SerializedName("homepage") val homepage: String?,
    @SerializedName("imdb_id") val imdbId: String?,
    @SerializedName("tagline") val tagline: String?
)

data class ProductionCountryDto(
    @SerializedName("iso_3166_1") val iso31661: String,
    @SerializedName("name") val name: String
)

data class ProductionCompanyDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("logo_path") val logoPath: String?,
    @SerializedName("origin_country") val originCountry: String
)

data class SpokenLanguageDto(
    @SerializedName("english_name") val englishName: String,
    @SerializedName("iso_639_1") val iso6391: String,
    @SerializedName("name") val name: String
)
