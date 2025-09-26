package com.example.filmix.data.mapper

import com.example.filmix.data.remote.dto.*
import com.example.filmix.domain.models.*

object DtoToDomainMapper {

    // MovieDto -> MovieItem
    fun MovieDto.toDomain(): MovieItem {
        return MovieItem(
            id = this.id,
            title = this.title,
            year = this.releaseDate?.take(4) ?: "",
            posterPath = this.posterPath ?: "",
            rating = this.voteAverage,
            backdropPath = this.backdropPath,
            mediaType = "movie",
            popularity = this.popularity
        )
    }

    // TvShowDto -> MovieItem
    fun TvShowDto.toDomain(): MovieItem {
        return MovieItem(
            id = this.id,
            title = this.name,
            year = this.firstAirDate?.take(4) ?: "",
            posterPath = this.posterPath ?: "",
            rating = this.voteAverage,
            backdropPath = this.backdropPath,
            mediaType = "tv",
            popularity = this.popularity
        )
    }

    // MovieDetailsDto -> MovieDetails
    fun MovieDetailsDto.toDomain(): MovieDetails {
        return MovieDetails(
            id = this.id,
            title = this.title,
            year = this.releaseDate?.take(4) ?: "",
            genres = this.genres.map { it.toDomain() },
            productionCountries = this.productionCountries.map { it.toDomain() },
            runtime = this.runtime ?: 0,
            episodeRunTime = emptyList(),
            numberOfSeasons = 0,
            status = this.status ?: "Released",
            overview = this.overview ?: ""
        )
    }

    // TvDetailsDto -> MovieDetails
    fun TvDetailsDto.toDomain(): MovieDetails {
        return MovieDetails(
            id = this.id,
            title = this.name,
            year = this.firstAirDate?.take(4) ?: "",
            genres = this.genres.map { it.toDomain() },
            productionCountries = this.productionCountries.map { it.toDomain() },
            runtime = 0,
            episodeRunTime = this.episodeRunTime ?: emptyList(),
            numberOfSeasons = this.numberOfSeasons ?: 0,
            status = this.status ?: "Unknown",
            overview = this.overview ?: ""
        )
    }

    // GenreDto -> Genre
    fun GenreDto.toDomain(): Genre {
        return Genre(
            id = this.id,
            name = this.name
        )
    }

    // ProductionCountryDto -> ProductionCountry
    fun ProductionCountryDto.toDomain(): ProductionCountry {
        return ProductionCountry(
            iso31661 = this.iso31661,
            name = this.name
        )
    }

    // CountryDto -> Country
    fun CountryDto.toDomain(): Country {
        return Country(
            iso_3166_1 = this.iso31661,
            english_name = this.englishName,
            native_name = this.nativeName
        )
    }

    // ImageResponse mapping - ИСПОЛЬЗУЕМ ПОЛНЫЕ ПУТИ
    fun com.example.filmix.data.remote.dto.ImageResponse.toDomainImageResponse(): com.example.filmix.domain.models.ImageResponse {
        return com.example.filmix.domain.models.ImageResponse(
            backdrops = this.backdrops.map { it.toDomain() },
            posters = this.posters.map { it.toDomain() }
        )
    }

    // ImageInfoDto -> ImageInfo
    fun ImageInfoDto.toDomain(): ImageInfo {
        return ImageInfo(
            filePath = this.filePath,
            aspectRatio = this.aspectRatio,
            height = this.height,
            width = this.width,
            voteAverage = this.voteAverage,
            voteCount = this.voteCount
        )
    }

    // VideoResponse mapping - ИСПОЛЬЗУЕМ ПОЛНЫЕ ПУТИ
    fun com.example.filmix.data.remote.dto.VideoResponse.toDomainVideoResponse(): com.example.filmix.domain.models.VideoResponse {
        return com.example.filmix.domain.models.VideoResponse(
            results = this.results.map { it.toDomain() }
        )
    }

    // VideoResultDto -> VideoResult
    fun VideoResultDto.toDomain(): VideoResult {
        return VideoResult(
            id = this.id,
            iso_639_1 = this.iso6391,
            iso_3166_1 = this.iso31661,
            key = this.key,
            name = this.name,
            official = this.official,
            published_at = this.publishedAt ?: "",
            site = this.site,
            size = this.size,
            type = this.type
        )
    }

    // CreditsResponse mapping - ИСПОЛЬЗУЕМ ПОЛНЫЕ ПУТИ
    fun com.example.filmix.data.remote.dto.CreditsResponse.toDomainCreditsResponse(): com.example.filmix.domain.models.CreditsResponse {
        return com.example.filmix.domain.models.CreditsResponse(
            cast = this.cast.map { it.toDomain() },
            crew = this.crew.map { it.toDomain() }
        )
    }

    // CastMemberDto -> CastMember
    fun CastMemberDto.toDomain(): CastMember {
        return CastMember(
            id = this.id,
            name = this.name,
            character = this.character,
            profilePath = this.profilePath ?: ""
        )
    }

    // CrewMemberDto -> CrewMember
    fun CrewMemberDto.toDomain(): CrewMember {
        return CrewMember(
            id = this.id,
            name = this.name,
            job = this.job,
            profilePath = this.profilePath ?: ""
        )
    }

    // PersonDetailsDto -> PersonDetails
    fun PersonDetailsDto.toDomain(): PersonDetails {
        return PersonDetails(
            id = this.id,
            name = this.name,
            biography = this.biography,
            birthday = this.birthday,
            placeOfBirth = this.placeOfBirth,
            profilePath = this.profilePath
        )
    }

    // SearchResultDto -> MovieItem
    fun SearchResultDto.toDomain(): MovieItem? {
        return when (this.mediaType) {
            "movie" -> MovieItem(
                id = this.id,
                title = this.title ?: "No Title",
                year = this.releaseDate?.take(4) ?: "",
                posterPath = this.posterPath ?: "",
                rating = this.voteAverage,
                backdropPath = this.backdropPath,
                mediaType = "movie",
                popularity = this.popularity
            )
            "tv" -> MovieItem(
                id = this.id,
                title = this.name ?: "No Title",
                year = this.firstAirDate?.take(4) ?: "",
                posterPath = this.posterPath ?: "",
                rating = this.voteAverage,
                backdropPath = this.backdropPath,
                mediaType = "tv",
                popularity = this.popularity
            )
            else -> null
        }
    }
}
