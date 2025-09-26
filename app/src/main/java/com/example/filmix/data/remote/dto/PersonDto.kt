package com.example.filmix.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PersonDetailsDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("biography") val biography: String?,
    @SerializedName("birthday") val birthday: String?,
    @SerializedName("deathday") val deathday: String?,
    @SerializedName("place_of_birth") val placeOfBirth: String?,
    @SerializedName("profile_path") val profilePath: String?,
    @SerializedName("known_for_department") val knownForDepartment: String?,
    @SerializedName("gender") val gender: Int,
    @SerializedName("popularity") val popularity: Double,
    @SerializedName("imdb_id") val imdbId: String?,
    @SerializedName("homepage") val homepage: String?
)

data class PersonMovieCreditsDto(
    @SerializedName("id") val id: Int,
    @SerializedName("cast") val cast: List<PersonMovieCastDto>,
    @SerializedName("crew") val crew: List<PersonMovieCrewDto>
)

data class PersonMovieCastDto(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("original_title") val originalTitle: String?,
    @SerializedName("overview") val overview: String?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("backdrop_path") val backdropPath: String?,
    @SerializedName("release_date") val releaseDate: String?,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("popularity") val popularity: Double,
    @SerializedName("character") val character: String,
    @SerializedName("credit_id") val creditId: String,
    @SerializedName("order") val order: Int
)

data class PersonMovieCrewDto(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("original_title") val originalTitle: String?,
    @SerializedName("overview") val overview: String?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("backdrop_path") val backdropPath: String?,
    @SerializedName("release_date") val releaseDate: String?,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("popularity") val popularity: Double,
    @SerializedName("job") val job: String,
    @SerializedName("department") val department: String,
    @SerializedName("credit_id") val creditId: String
)

data class PersonTvCreditsDto(
    @SerializedName("id") val id: Int,
    @SerializedName("cast") val cast: List<PersonTvCastDto>,
    @SerializedName("crew") val crew: List<PersonTvCrewDto>
)

data class PersonTvCastDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("original_name") val originalName: String?,
    @SerializedName("overview") val overview: String?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("backdrop_path") val backdropPath: String?,
    @SerializedName("first_air_date") val firstAirDate: String?,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("popularity") val popularity: Double,
    @SerializedName("character") val character: String,
    @SerializedName("credit_id") val creditId: String,
    @SerializedName("episode_count") val episodeCount: Int
)

data class PersonTvCrewDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("original_name") val originalName: String?,
    @SerializedName("overview") val overview: String?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("backdrop_path") val backdropPath: String?,
    @SerializedName("first_air_date") val firstAirDate: String?,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("popularity") val popularity: Double,
    @SerializedName("job") val job: String,
    @SerializedName("department") val department: String,
    @SerializedName("credit_id") val creditId: String,
    @SerializedName("episode_count") val episodeCount: Int
)
