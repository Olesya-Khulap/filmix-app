package com.example.filmix.data.remote.dto

import com.google.gson.annotations.SerializedName

data class GenreDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
)

data class GenreListResponse(
    @SerializedName("genres") val genres: List<GenreDto>
)

data class CountryDto(
    @SerializedName("iso_3166_1") val iso31661: String,
    @SerializedName("english_name") val englishName: String,
    @SerializedName("native_name") val nativeName: String
)

data class ImageResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("backdrops") val backdrops: List<ImageInfoDto>,
    @SerializedName("posters") val posters: List<ImageInfoDto>
)

data class ImageInfoDto(
    @SerializedName("aspect_ratio") val aspectRatio: Double,
    @SerializedName("file_path") val filePath: String,
    @SerializedName("height") val height: Int,
    @SerializedName("width") val width: Int,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("vote_count") val voteCount: Int
)

data class VideoResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("results") val results: List<VideoResultDto>
)

data class VideoResultDto(
    @SerializedName("id") val id: String,
    @SerializedName("iso_639_1") val iso6391: String,
    @SerializedName("iso_3166_1") val iso31661: String,
    @SerializedName("key") val key: String,
    @SerializedName("name") val name: String,
    @SerializedName("official") val official: Boolean,
    @SerializedName("published_at") val publishedAt: String?,
    @SerializedName("site") val site: String,
    @SerializedName("size") val size: Int,
    @SerializedName("type") val type: String
)

data class CreditsResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("cast") val cast: List<CastMemberDto>,
    @SerializedName("crew") val crew: List<CrewMemberDto>
)

data class CastMemberDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("character") val character: String,
    @SerializedName("profile_path") val profilePath: String?,
    @SerializedName("order") val order: Int,
    @SerializedName("cast_id") val castId: Int?,
    @SerializedName("credit_id") val creditId: String
)

data class CrewMemberDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("job") val job: String,
    @SerializedName("department") val department: String,
    @SerializedName("profile_path") val profilePath: String?,
    @SerializedName("credit_id") val creditId: String
)

data class SearchResponse(
    @SerializedName("page") val page: Int,
    @SerializedName("results") val results: List<SearchResultDto>,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("total_results") val totalResults: Int
)

data class SearchResultDto(
    @SerializedName("id") val id: Int,
    @SerializedName("media_type") val mediaType: String,
    @SerializedName("title") val title: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("original_title") val originalTitle: String?,
    @SerializedName("original_name") val originalName: String?,
    @SerializedName("overview") val overview: String?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("backdrop_path") val backdropPath: String?,
    @SerializedName("release_date") val releaseDate: String?,
    @SerializedName("first_air_date") val firstAirDate: String?,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("popularity") val popularity: Double,
    @SerializedName("genre_ids") val genreIds: List<Int>?
)
