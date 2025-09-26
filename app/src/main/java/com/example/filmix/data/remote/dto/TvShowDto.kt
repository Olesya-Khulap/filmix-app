package com.example.filmix.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TvShowListResponse(
    @SerializedName("page") val page: Int,
    @SerializedName("results") val results: List<TvShowDto>,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("total_results") val totalResults: Int
)

data class TvShowDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("original_name") val originalName: String?,
    @SerializedName("overview") val overview: String?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("backdrop_path") val backdropPath: String?,
    @SerializedName("first_air_date") val firstAirDate: String?,
    @SerializedName("last_air_date") val lastAirDate: String?,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("vote_count") val voteCount: Int,
    @SerializedName("popularity") val popularity: Double,
    @SerializedName("genre_ids") val genreIds: List<Int>?,
    @SerializedName("original_language") val originalLanguage: String?,
    @SerializedName("origin_country") val originCountry: List<String>?
)

data class TvDetailsDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("original_name") val originalName: String?,
    @SerializedName("overview") val overview: String?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("backdrop_path") val backdropPath: String?,
    @SerializedName("first_air_date") val firstAirDate: String?,
    @SerializedName("last_air_date") val lastAirDate: String?,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("vote_count") val voteCount: Int,
    @SerializedName("popularity") val popularity: Double,
    @SerializedName("genres") val genres: List<GenreDto>,
    @SerializedName("production_countries") val productionCountries: List<ProductionCountryDto>,
    @SerializedName("production_companies") val productionCompanies: List<ProductionCompanyDto>?,
    @SerializedName("spoken_languages") val spokenLanguages: List<SpokenLanguageDto>?,
    @SerializedName("homepage") val homepage: String?,
    @SerializedName("status") val status: String?,
    @SerializedName("type") val type: String?,
    @SerializedName("number_of_episodes") val numberOfEpisodes: Int?,
    @SerializedName("number_of_seasons") val numberOfSeasons: Int?,
    @SerializedName("episode_run_time") val episodeRunTime: List<Int>?,
    @SerializedName("in_production") val inProduction: Boolean?,
    @SerializedName("languages") val languages: List<String>?,
    @SerializedName("networks") val networks: List<NetworkDto>?,
    @SerializedName("seasons") val seasons: List<SeasonDto>?
)

data class NetworkDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("logo_path") val logoPath: String?,
    @SerializedName("origin_country") val originCountry: String
)

data class SeasonDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("overview") val overview: String?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("season_number") val seasonNumber: Int,
    @SerializedName("episode_count") val episodeCount: Int,
    @SerializedName("air_date") val airDate: String?
)
