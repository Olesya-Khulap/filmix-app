package com.example.filmix.data.remote

import android.util.Log
import com.example.filmix.data.remote.api.ApiService
import com.example.filmix.data.mapper.DtoToDomainMapper.toDomain
import com.example.filmix.data.mapper.DtoToDomainMapper.toDomainImageResponse
import com.example.filmix.data.mapper.DtoToDomainMapper.toDomainVideoResponse
import com.example.filmix.data.mapper.DtoToDomainMapper.toDomainCreditsResponse
import com.example.filmix.domain.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TmdbApiService @Inject constructor(
    private val apiService: ApiService
) {

    companion object {
        private const val TAG = "TmdbApiService"
    }

    suspend fun getPopularMovies(page: Int = 1): List<MovieItem> = withContext(Dispatchers.IO) {
        Log.d(TAG, "Fetching popular movies, page: $page")
        try {
            val response = apiService.getPopularMovies(page)
            Log.d(TAG, "Popular movies API response: isSuccessful=${response.isSuccessful}, code=${response.code()}")
            if (response.isSuccessful) {
                val movies = response.body()?.results?.map { it.toDomain() } ?: emptyList()
                Log.d(TAG, "Popular movies fetched successfully: ${movies.size} movies")
                movies.forEach { movie ->
                    Log.v(TAG, "Movie: ${movie.title} (${movie.year}) - Rating: ${movie.rating}")
                }
                movies
            } else {
                Log.e(TAG, "Popular movies API error: ${response.code()} - ${response.message()}")
                Log.e(TAG, "Error body: ${response.errorBody()?.string()}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Network error fetching popular movies", e)
            emptyList()
        }
    }

    suspend fun getPopularTvShows(page: Int = 1): List<MovieItem> = withContext(Dispatchers.IO) {
        Log.d(TAG, "Fetching popular TV shows, page: $page")
        try {
            val response = apiService.getPopularTvShows(page)
            Log.d(TAG, "Popular TV shows API response: isSuccessful=${response.isSuccessful}, code=${response.code()}")
            if (response.isSuccessful) {
                val shows = response.body()?.results?.map { it.toDomain() } ?: emptyList()
                Log.d(TAG, "Popular TV shows fetched successfully: ${shows.size} shows")
                shows
            } else {
                Log.e(TAG, "Popular TV shows API error: ${response.code()} - ${response.message()}")
                Log.e(TAG, "Error body: ${response.errorBody()?.string()}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Network error fetching popular TV shows", e)
            emptyList()
        }
    }

    suspend fun getNowPlayingMovies(page: Int = 1): List<MovieItem> = withContext(Dispatchers.IO) {
        Log.d(TAG, "Fetching now playing movies, page: $page")
        try {
            val response = apiService.getNowPlayingMovies(page)
            Log.d(TAG, "Now playing API response: isSuccessful=${response.isSuccessful}, code=${response.code()}")
            if (response.isSuccessful) {
                val movies = response.body()?.results?.map { it.toDomain() } ?: emptyList()
                Log.d(TAG, "Now playing movies fetched successfully: ${movies.size} movies")
                movies
            } else {
                Log.e(TAG, "Now playing API error: ${response.code()} - ${response.message()}")
                Log.e(TAG, "Error body: ${response.errorBody()?.string()}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Network error fetching now playing movies", e)
            emptyList()
        }
    }

    suspend fun getTopRatedMovies(page: Int = 1): List<MovieItem> = withContext(Dispatchers.IO) {
        Log.d(TAG, "Fetching top rated movies, page: $page")
        try {
            val response = apiService.getTopRatedMovies(page)
            Log.d(TAG, "Top rated API response: isSuccessful=${response.isSuccessful}, code=${response.code()}")
            if (response.isSuccessful) {
                val movies = response.body()?.results?.map { it.toDomain() } ?: emptyList()
                Log.d(TAG, "Top rated movies fetched successfully: ${movies.size} movies")
                movies
            } else {
                Log.e(TAG, "Top rated API error: ${response.code()} - ${response.message()}")
                Log.e(TAG, "Error body: ${response.errorBody()?.string()}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Network error fetching top rated movies", e)
            emptyList()
        }
    }

    suspend fun getMovieDetails(movieId: Int): MovieDetails = withContext(Dispatchers.IO) {
        Log.d(TAG, "Fetching movie details for ID: $movieId")
        try {
            val response = apiService.getMovieDetails(movieId)
            Log.d(TAG, "Movie details API response: isSuccessful=${response.isSuccessful}, code=${response.code()}")
            if (response.isSuccessful) {
                val details = response.body()!!.toDomain()
                Log.d(TAG, "Movie details fetched successfully: ${details.title}")
                details
            } else {
                Log.e(TAG, "Movie details API error: ${response.code()} - ${response.message()}")
                Log.e(TAG, "Error body: ${response.errorBody()?.string()}")
                throw Exception("Failed to get movie details: ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching movie details for ID: $movieId", e)
            throw e
        }
    }

    suspend fun getTvDetails(tvId: Int): MovieDetails = withContext(Dispatchers.IO) {
        Log.d(TAG, "Fetching TV details for ID: $tvId")
        try {
            val response = apiService.getTvDetails(tvId)
            Log.d(TAG, "TV details API response: isSuccessful=${response.isSuccessful}, code=${response.code()}")
            if (response.isSuccessful) {
                val details = response.body()!!.toDomain()
                Log.d(TAG, "TV details fetched successfully: ${details.title}")
                details
            } else {
                Log.e(TAG, "TV details API error: ${response.code()} - ${response.message()}")
                Log.e(TAG, "Error body: ${response.errorBody()?.string()}")
                throw Exception("Failed to get TV details: ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching TV details for ID: $tvId", e)
            throw e
        }
    }

    suspend fun getMovieImages(movieId: Int): ImageResponse = withContext(Dispatchers.IO) {
        Log.d(TAG, "Fetching movie images for ID: $movieId")
        try {
            val response = apiService.getMovieImages(movieId)
            Log.d(TAG, "Movie images API response: isSuccessful=${response.isSuccessful}")
            if (response.isSuccessful) {
                val images = response.body()!!.toDomainImageResponse()
                Log.d(TAG, "Movie images fetched: ${images.backdrops.size} backdrops, ${images.posters.size} posters")
                images
            } else {
                Log.e(TAG, "Movie images API error: ${response.code()} - ${response.message()}")
                ImageResponse(emptyList(), emptyList())
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching movie images for ID: $movieId", e)
            ImageResponse(emptyList(), emptyList())
        }
    }

    suspend fun getTvImages(tvId: Int): ImageResponse = withContext(Dispatchers.IO) {
        Log.d(TAG, "Fetching TV images for ID: $tvId")
        try {
            val response = apiService.getTvImages(tvId)
            Log.d(TAG, "TV images API response: isSuccessful=${response.isSuccessful}")
            if (response.isSuccessful) {
                val images = response.body()!!.toDomainImageResponse()
                Log.d(TAG, "TV images fetched: ${images.backdrops.size} backdrops, ${images.posters.size} posters")
                images
            } else {
                Log.e(TAG, "TV images API error: ${response.code()} - ${response.message()}")
                ImageResponse(emptyList(), emptyList())
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching TV images for ID: $tvId", e)
            ImageResponse(emptyList(), emptyList())
        }
    }

    suspend fun getMovieVideos(movieId: Int): VideoResponse = withContext(Dispatchers.IO) {
        Log.d(TAG, "Fetching movie videos for ID: $movieId")
        try {
            val response = apiService.getMovieVideos(movieId)
            Log.d(TAG, "Movie videos API response: isSuccessful=${response.isSuccessful}")
            if (response.isSuccessful) {
                val videos = response.body()!!.toDomainVideoResponse()
                Log.d(TAG, "Movie videos fetched: ${videos.results.size} videos")
                videos
            } else {
                Log.e(TAG, "Movie videos API error: ${response.code()} - ${response.message()}")
                VideoResponse(emptyList())
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching movie videos for ID: $movieId", e)
            VideoResponse(emptyList())
        }
    }

    suspend fun getTvVideos(tvId: Int): VideoResponse = withContext(Dispatchers.IO) {
        Log.d(TAG, "Fetching TV videos for ID: $tvId")
        try {
            val response = apiService.getTvVideos(tvId)
            Log.d(TAG, "TV videos API response: isSuccessful=${response.isSuccessful}")
            if (response.isSuccessful) {
                val videos = response.body()!!.toDomainVideoResponse()
                Log.d(TAG, "TV videos fetched: ${videos.results.size} videos")
                videos
            } else {
                Log.e(TAG, "TV videos API error: ${response.code()} - ${response.message()}")
                VideoResponse(emptyList())
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching TV videos for ID: $tvId", e)
            VideoResponse(emptyList())
        }
    }

    suspend fun getMovieCredits(movieId: Int): CreditsResponse = withContext(Dispatchers.IO) {
        Log.d(TAG, "Fetching movie credits for ID: $movieId")
        try {
            val response = apiService.getMovieCredits(movieId)
            Log.d(TAG, "Movie credits API response: isSuccessful=${response.isSuccessful}")
            if (response.isSuccessful) {
                val credits = response.body()!!.toDomainCreditsResponse()
                Log.d(TAG, "Movie credits fetched: ${credits.cast.size} cast, ${credits.crew.size} crew")
                credits
            } else {
                Log.e(TAG, "Movie credits API error: ${response.code()} - ${response.message()}")
                CreditsResponse(emptyList(), emptyList())
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching movie credits for ID: $movieId", e)
            CreditsResponse(emptyList(), emptyList())
        }
    }

    suspend fun getTvCredits(tvId: Int): CreditsResponse = withContext(Dispatchers.IO) {
        Log.d(TAG, "Fetching TV credits for ID: $tvId")
        try {
            val response = apiService.getTvCredits(tvId)
            Log.d(TAG, "TV credits API response: isSuccessful=${response.isSuccessful}")
            if (response.isSuccessful) {
                val credits = response.body()!!.toDomainCreditsResponse()
                Log.d(TAG, "TV credits fetched: ${credits.cast.size} cast, ${credits.crew.size} crew")
                credits
            } else {
                Log.e(TAG, "TV credits API error: ${response.code()} - ${response.message()}")
                CreditsResponse(emptyList(), emptyList())
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching TV credits for ID: $tvId", e)
            CreditsResponse(emptyList(), emptyList())
        }
    }

    suspend fun searchMulti(query: String, page: Int = 1): List<MovieItem> = withContext(Dispatchers.IO) {
        Log.d(TAG, "Searching for: '$query', page: $page")
        try {
            val response = apiService.searchMulti(query, page)
            Log.d(TAG, "Search API response: isSuccessful=${response.isSuccessful}")
            if (response.isSuccessful) {
                val results = response.body()?.results?.mapNotNull { it.toDomain() } ?: emptyList()
                Log.d(TAG, "Search results fetched: ${results.size} items")
                results
            } else {
                Log.e(TAG, "Search API error: ${response.code()} - ${response.message()}")
                Log.e(TAG, "Error body: ${response.errorBody()?.string()}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error searching for: '$query'", e)
            emptyList()
        }
    }

    suspend fun getMovieGenres(): List<Genre> = withContext(Dispatchers.IO) {
        Log.d(TAG, "Fetching movie genres")
        try {
            val response = apiService.getMovieGenres()
            Log.d(TAG, "Movie genres API response: isSuccessful=${response.isSuccessful}")
            if (response.isSuccessful) {
                val genres = response.body()?.genres?.map { it.toDomain() } ?: emptyList()
                Log.d(TAG, "Movie genres fetched: ${genres.size} genres")
                genres
            } else {
                Log.e(TAG, "Movie genres API error: ${response.code()} - ${response.message()}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching movie genres", e)
            emptyList()
        }
    }

    suspend fun getTvGenres(): List<Genre> = withContext(Dispatchers.IO) {
        Log.d(TAG, "Fetching TV genres")
        try {
            val response = apiService.getTvGenres()
            Log.d(TAG, "TV genres API response: isSuccessful=${response.isSuccessful}")
            if (response.isSuccessful) {
                val genres = response.body()?.genres?.map { it.toDomain() } ?: emptyList()
                Log.d(TAG, "TV genres fetched: ${genres.size} genres")
                genres
            } else {
                Log.e(TAG, "TV genres API error: ${response.code()} - ${response.message()}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching TV genres", e)
            emptyList()
        }
    }

    suspend fun getPersonDetails(personId: Int): PersonDetails = withContext(Dispatchers.IO) {
        Log.d(TAG, "Fetching person details for ID: $personId")
        try {
            val response = apiService.getPersonDetails(personId)
            Log.d(TAG, "Person details API response: isSuccessful=${response.isSuccessful}")
            if (response.isSuccessful) {
                val dto = response.body()!!
                val details = PersonDetails(
                    id = dto.id,
                    name = dto.name,
                    biography = dto.biography,
                    birthday = dto.birthday,
                    placeOfBirth = dto.placeOfBirth,
                    profilePath = dto.profilePath
                )
                Log.d(TAG, "Person details fetched: ${details.name}")
                details
            } else {
                Log.e(TAG, "Person details API error: ${response.code()} - ${response.message()}")
                throw Exception("Failed to get person details: ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching person details for ID: $personId", e)
            throw e
        }
    }

    suspend fun getPersonFilmography(personId: Int): List<MovieItem> = withContext(Dispatchers.IO) {
        Log.d(TAG, "Fetching person filmography for ID: $personId")
        try {
            val response = apiService.getPersonMovieCredits(personId)
            Log.d(TAG, "Person filmography API response: isSuccessful=${response.isSuccessful}")
            if (response.isSuccessful) {
                val movies = response.body()?.cast?.take(15)?.map { castDto ->
                    MovieItem(
                        id = castDto.id,
                        title = castDto.title,
                        year = castDto.releaseDate?.take(4) ?: "N/A",
                        posterPath = castDto.posterPath ?: "",
                        rating = castDto.voteAverage,
                        backdropPath = castDto.backdropPath,
                        mediaType = "movie"
                    )
                } ?: emptyList()
                Log.d(TAG, "Person filmography fetched: ${movies.size} movies")
                movies
            } else {
                Log.e(TAG, "Person filmography API error: ${response.code()} - ${response.message()}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching person filmography for ID: $personId", e)
            emptyList()
        }
    }
    
    suspend fun getBannerMovies(): List<MovieItem> {
        Log.d(TAG, "Creating banner movies from popular movies")
        val popularMovies = getPopularMovies().shuffled().take(3)
        Log.d(TAG, "Banner movies selected: ${popularMovies.size} movies")
        return popularMovies.map { movie ->
            val backdrops = getMovieImages(movie.id)
            movie.copy(backdropPath = backdrops.backdrops.firstOrNull()?.filePath)
        }
    }

    suspend fun getAllMoviesPage(page: Int): List<MovieItem> {
        Log.d(TAG, "Getting all movies page: $page")
        return getPopularMovies(page)
    }

    suspend fun getMediaForCategory(categoryType: CategoryType): List<MovieItem> {
        Log.d(TAG, "Getting media for category: $categoryType")
        return when (categoryType) {
            CategoryType.POPULARMOVIES -> getPopularMovies()
            CategoryType.POPULARSERIES -> getPopularTvShows()
            CategoryType.NEWRELEASES -> getNowPlayingMovies()
            CategoryType.TOPRATED -> getTopRatedMovies()

            CategoryType.CUSTOM_FAVOURITES,
            CategoryType.CUSTOM_WATCH_LATER,
            CategoryType.CUSTOM_VIEWED -> {
                Log.d(TAG, "Custom category $categoryType should be handled by GetUserListsUseCase, not TmdbApiService")
                emptyList()
            }
        }
    }

    suspend fun getCountries(): List<Country> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getCountries()
            if (response.isSuccessful) {
                response.body()?.map { dto ->
                    Country(
                        iso_3166_1 = dto.iso31661,
                        english_name = dto.englishName,
                        native_name = dto.nativeName
                    )
                } ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getFilteredMovies(page: Int, filters: MovieFilters): List<MovieItem> = withContext(Dispatchers.IO) {
        try {
            val genreIds = if (filters.genreIds.isNotEmpty()) filters.genreIds.joinToString(",") else null
            val countryIds = if (filters.countryIds.isNotEmpty()) filters.countryIds.joinToString(",") else null
            val yearFrom = filters.yearFrom?.let { "${it}-01-01" }
            val yearTo = filters.yearTo?.let { "${it}-12-31" }

            val response = apiService.discoverMovies(
                page = page,
                genreIds = genreIds,
                countries = countryIds,
                minRating = filters.minRating,
                maxRating = filters.maxRating,
                yearFrom = yearFrom,
                yearTo = yearTo
            )

            if (response.isSuccessful) {
                response.body()?.results?.map { it.toDomain() } ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getFilteredTvShows(page: Int, filters: ShowFilters): List<MovieItem> = withContext(Dispatchers.IO) {
        try {
            val genreIds = if (filters.genreIds.isNotEmpty()) filters.genreIds.joinToString(",") else null
            val countryIds = if (filters.countryIds.isNotEmpty()) filters.countryIds.joinToString(",") else null
            val yearFrom = filters.yearFrom?.let { "${it}-01-01" }
            val yearTo = filters.yearTo?.let { "${it}-12-31" }

            // Converting statuses for TMDB API
            val status = when {
                filters.statuses.contains("Ended") && !filters.statuses.contains("Returning Series") -> "Ended"
                !filters.statuses.contains("Ended") && filters.statuses.contains("Returning Series") -> "Returning Series"
                else -> null
            }

            val response = apiService.discoverTvShows(
                page = page,
                genreIds = genreIds,
                countries = countryIds,
                minRating = filters.minRating,
                maxRating = filters.maxRating,
                yearFrom = yearFrom,
                yearTo = yearTo,
                status = status
            )

            if (response.isSuccessful) {
                response.body()?.results?.map { it.toDomain() } ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun searchMovies(query: String, page: Int = 1): List<MovieItem> = withContext(Dispatchers.IO) {
        Log.d(TAG, "Searching movies for: '$query', page: $page")
        try {
            val response = apiService.searchMovies(query, page)
            Log.d(TAG, "Search movies API response: isSuccessful=${response.isSuccessful}")
            if (response.isSuccessful) {
                val results = response.body()?.results?.map { it.toDomain() } ?: emptyList()
                Log.d(TAG, "Search movies results fetched: ${results.size} movies")
                results
            } else {
                Log.e(TAG, "Search movies API error: ${response.code()} - ${response.message()}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error searching movies for: '$query'", e)
            emptyList()
        }
    }

    suspend fun searchTvShows(query: String, page: Int = 1): List<MovieItem> = withContext(Dispatchers.IO) {
        Log.d(TAG, "Searching TV shows for: '$query', page: $page")
        try {
            val response = apiService.searchTv(query, page)
            Log.d(TAG, "Search TV shows API response: isSuccessful=${response.isSuccessful}")
            if (response.isSuccessful) {
                val results = response.body()?.results?.map { it.toDomain() } ?: emptyList() // ИСПРАВЛЕНО: убрал .results.it
                Log.d(TAG, "Search TV shows results fetched: ${results.size} shows")
                results
            } else {
                Log.e(TAG, "Search TV shows API error: ${response.code()} - ${response.message()}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error searching TV shows for: '$query'", e)
            emptyList()
        }
    }


    suspend fun searchWithFilters(query: String, page: Int, filters: SearchFilters): List<MovieItem> = withContext(Dispatchers.IO) {
        try {
            val results = mutableListOf<MovieItem>()

            val typesToProcess = if (filters.types.isEmpty()) listOf("movie", "tv") else filters.types

            if ("movie" in typesToProcess) {
                val movieFilters = MovieFilters(
                    genreIds = filters.genreIds,
                    countryIds = filters.countryIds,
                    minRating = filters.minRating,
                    maxRating = filters.maxRating,
                    yearFrom = filters.yearFrom,
                    yearTo = filters.yearTo
                )

                val movies = if (query.isNotEmpty()) {
                    searchMovies(query, page).filter { movie ->
                        matchesFilters(movie, movieFilters)
                    }
                } else {
                    getFilteredMovies(page, movieFilters)
                }
                results.addAll(movies)
            }

            if ("tv" in typesToProcess) {
                val showFilters = ShowFilters(
                    genreIds = filters.genreIds,
                    countryIds = filters.countryIds,
                    minRating = filters.minRating,
                    maxRating = filters.maxRating,
                    yearFrom = filters.yearFrom,
                    yearTo = filters.yearTo,
                    statuses = filters.statuses
                )

                val shows = if (query.isNotEmpty()) {
                    searchTvShows(query, page).filter { show ->
                        matchesShowFilters(show, showFilters)
                    }
                } else {
                    getFilteredTvShows(page, showFilters)
                }
                results.addAll(shows)
            }

            results.shuffled()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    private fun matchesFilters(movie: MovieItem, filters: MovieFilters): Boolean {
        if (filters.minRating != null && movie.rating < filters.minRating) return false
        if (filters.maxRating != null && movie.rating > filters.maxRating) return false

        val year = movie.year.toIntOrNull()
        if (filters.yearFrom != null && (year == null || year < filters.yearFrom)) return false
        if (filters.yearTo != null && (year == null || year > filters.yearTo)) return false

        return true
    }

    private fun matchesShowFilters(show: MovieItem, filters: ShowFilters): Boolean {
        if (filters.minRating != null && show.rating < filters.minRating) return false
        if (filters.maxRating != null && show.rating > filters.maxRating) return false

        val year = show.year.toIntOrNull()
        if (filters.yearFrom != null && (year == null || year < filters.yearFrom)) return false
        if (filters.yearTo != null && (year == null || year > filters.yearTo)) return false

        return true
    }


}
