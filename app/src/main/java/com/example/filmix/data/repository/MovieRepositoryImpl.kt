package com.example.filmix.data.repository

import com.example.filmix.data.local.dao.MovieDao
import com.example.filmix.data.local.dao.GenreDao
import com.example.filmix.data.remote.TmdbApiService
import com.example.filmix.data.mapper.EntityToDomainMapper.toDomain
import com.example.filmix.data.mapper.DomainToEntityMapper.toEntity
import com.example.filmix.data.mapper.DomainToEntityMapper.toEntity as toGenreEntity
import com.example.filmix.domain.models.*
import com.example.filmix.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepositoryImpl @Inject constructor(
    private val apiService: TmdbApiService,
    private val movieDao: MovieDao,
    private val genreDao: GenreDao
) : MovieRepository {

    companion object {
        private const val CACHE_EXPIRY_HOURS = 6
        private const val CACHE_EXPIRY_MILLIS = CACHE_EXPIRY_HOURS * 60 * 60 * 1000L
    }

    override suspend fun getPopularMovies(page: Int, forceRefresh: Boolean): List<MovieItem> {
        return if (forceRefresh || shouldRefreshCache()) {
            try {
                val movies = apiService.getPopularMovies(page)
                if (movies.isNotEmpty() && page == 1) {
                    movieDao.clearPopularMovies()
                    val entities = movies.map { it.toEntity(isPopular = true) }
                    movieDao.insertMovies(entities)
                }
                movies
            } catch (e: Exception) {
                e.printStackTrace()
                movieDao.getPopularMovies().map { it.toDomain() }
            }
        } else {
            val cachedMovies = movieDao.getPopularMovies()
            if (cachedMovies.isNotEmpty()) {
                cachedMovies.map { it.toDomain() }
            } else {
                getPopularMovies(page, forceRefresh = true)
            }
        }
    }

    override suspend fun getPopularTvShows(page: Int, forceRefresh: Boolean): List<MovieItem> {
        return if (forceRefresh || shouldRefreshCache()) {
            try {
                val shows = apiService.getPopularTvShows(page)
                if (shows.isNotEmpty() && page == 1) {
                    val entities = shows.map { it.toEntity(isPopular = true) }
                    movieDao.insertMovies(entities)
                }
                shows
            } catch (e: Exception) {
                e.printStackTrace()
                movieDao.getPopularMovies().filter { it.mediaType == "tv" }.map { it.toDomain() }
            }
        } else {
            val cachedShows = movieDao.getPopularMovies().filter { it.mediaType == "tv" }
            if (cachedShows.isNotEmpty()) {
                cachedShows.map { it.toDomain() }
            } else {
                getPopularTvShows(page, forceRefresh = true)
            }
        }
    }

    override suspend fun getNowPlayingMovies(page: Int, forceRefresh: Boolean): List<MovieItem> {
        return if (forceRefresh || shouldRefreshCache()) {
            try {
                val movies = apiService.getNowPlayingMovies(page)
                if (movies.isNotEmpty() && page == 1) {
                    movieDao.clearNowPlayingMovies()
                    val entities = movies.map { it.toEntity(isNowPlaying = true) }
                    movieDao.insertMovies(entities)
                }
                movies
            } catch (e: Exception) {
                e.printStackTrace()
                movieDao.getNowPlayingMovies().map { it.toDomain() }
            }
        } else {
            val cachedMovies = movieDao.getNowPlayingMovies()
            if (cachedMovies.isNotEmpty()) {
                cachedMovies.map { it.toDomain() }
            } else {
                getNowPlayingMovies(page, forceRefresh = true)
            }
        }
    }

    override suspend fun getTopRatedMovies(page: Int, forceRefresh: Boolean): List<MovieItem> {
        return if (forceRefresh || shouldRefreshCache()) {
            try {
                val movies = apiService.getTopRatedMovies(page)
                if (movies.isNotEmpty() && page == 1) {
                    movieDao.clearTopRatedMovies()
                    val entities = movies.map { it.toEntity(isTopRated = true) }
                    movieDao.insertMovies(entities)
                }
                movies
            } catch (e: Exception) {
                e.printStackTrace()
                movieDao.getTopRatedMovies().map { it.toDomain() }
            }
        } else {
            val cachedMovies = movieDao.getTopRatedMovies()
            if (cachedMovies.isNotEmpty()) {
                cachedMovies.map { it.toDomain() }
            } else {
                getTopRatedMovies(page, forceRefresh = true)
            }
        }
    }

    override suspend fun getBannerMovies(forceRefresh: Boolean): List<MovieItem> {
        return if (forceRefresh || shouldRefreshCache()) {
            try {
                val movies = apiService.getBannerMovies()
                if (movies.isNotEmpty()) {
                    movieDao.clearBannerMovies()
                    val entities = movies.map { it.toEntity(isBanner = true) }
                    movieDao.insertMovies(entities)
                }
                movies
            } catch (e: Exception) {
                e.printStackTrace()
                movieDao.getBannerMovies().map { it.toDomain() }
            }
        } else {
            val cachedMovies = movieDao.getBannerMovies()
            if (cachedMovies.isNotEmpty()) {
                cachedMovies.map { it.toDomain() }
            } else {
                getBannerMovies(forceRefresh = true)
            }
        }
    }

    override suspend fun getMovieDetails(movieId: Int, forceRefresh: Boolean): MovieDetails {
        return try {
            apiService.getMovieDetails(movieId)
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    override suspend fun getTvDetails(tvId: Int, forceRefresh: Boolean): MovieDetails {
        return try {
            apiService.getTvDetails(tvId)
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    override suspend fun getMovieImages(movieId: Int, forceRefresh: Boolean): ImageResponse {
        return try {
            apiService.getMovieImages(movieId)
        } catch (e: Exception) {
            e.printStackTrace()
            ImageResponse(emptyList(), emptyList())
        }
    }

    override suspend fun getTvImages(tvId: Int, forceRefresh: Boolean): ImageResponse {
        return try {
            apiService.getTvImages(tvId)
        } catch (e: Exception) {
            e.printStackTrace()
            ImageResponse(emptyList(), emptyList())
        }
    }

    override suspend fun getMovieVideos(movieId: Int, forceRefresh: Boolean): VideoResponse {
        return try {
            apiService.getMovieVideos(movieId)
        } catch (e: Exception) {
            e.printStackTrace()
            VideoResponse(emptyList())
        }
    }

    override suspend fun getTvVideos(tvId: Int, forceRefresh: Boolean): VideoResponse {
        return try {
            apiService.getTvVideos(tvId)
        } catch (e: Exception) {
            e.printStackTrace()
            VideoResponse(emptyList())
        }
    }

    override suspend fun getMovieCredits(movieId: Int, forceRefresh: Boolean): CreditsResponse {
        return try {
            apiService.getMovieCredits(movieId)
        } catch (e: Exception) {
            e.printStackTrace()
            CreditsResponse(emptyList(), emptyList())
        }
    }

    override suspend fun getTvCredits(tvId: Int, forceRefresh: Boolean): CreditsResponse {
        return try {
            apiService.getTvCredits(tvId)
        } catch (e: Exception) {
            e.printStackTrace()
            CreditsResponse(emptyList(), emptyList())
        }
    }

    override suspend fun searchMulti(query: String, page: Int): List<MovieItem> {
        return try {
            apiService.searchMulti(query, page)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun getMovieGenres(forceRefresh: Boolean): List<Genre> {
        return if (forceRefresh || shouldRefreshGenreCache()) {
            try {
                val genres = apiService.getMovieGenres()
                if (genres.isNotEmpty()) {
                    val entities = genres.map { it.toGenreEntity("movie") }
                    genreDao.deleteGenresByType("movie")
                    genreDao.insertGenres(entities)
                }
                genres
            } catch (e: Exception) {
                e.printStackTrace()
                genreDao.getGenresByType("movie").map { it.toDomain() }
            }
        } else {
            val cachedGenres = genreDao.getGenresByType("movie")
            if (cachedGenres.isNotEmpty()) {
                cachedGenres.map { it.toDomain() }
            } else {
                getMovieGenres(forceRefresh = true)
            }
        }
    }

    override suspend fun getTvGenres(forceRefresh: Boolean): List<Genre> {
        return if (forceRefresh || shouldRefreshGenreCache()) {
            try {
                val genres = apiService.getTvGenres()
                if (genres.isNotEmpty()) {
                    val entities = genres.map { it.toGenreEntity("tv") }
                    genreDao.deleteGenresByType("tv")
                    genreDao.insertGenres(entities)
                }
                genres
            } catch (e: Exception) {
                e.printStackTrace()
                genreDao.getGenresByType("tv").map { it.toDomain() }
            }
        } else {
            val cachedGenres = genreDao.getGenresByType("tv")
            if (cachedGenres.isNotEmpty()) {
                cachedGenres.map { it.toDomain() }
            } else {
                getTvGenres(forceRefresh = true)
            }
        }
    }

    // НОВЫЕ МЕТОДЫ ДЛЯ ФИЛЬТРОВ
    override suspend fun getCountries(): List<Country> {
        return try {
            apiService.getCountries()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun getFilteredMovies(page: Int, filters: MovieFilters): List<MovieItem> {
        return try {
            apiService.getFilteredMovies(page, filters)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun getPersonDetails(personId: Int): PersonDetails {
        return try {
            apiService.getPersonDetails(personId)
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    override suspend fun getPersonFilmography(personId: Int): List<MovieItem> {
        return try {
            apiService.getPersonFilmography(personId)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override fun getPopularMoviesFlow(): Flow<List<MovieItem>> {
        return movieDao.getPopularMoviesFlow().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getCachedMovieById(movieId: Int): Flow<MovieItem?> {
        return kotlinx.coroutines.flow.flow {
            val entity = movieDao.getMovieById(movieId)
            emit(entity?.toDomain())
        }
    }

    override suspend fun clearOldCache() {
        val cutoffTime = System.currentTimeMillis() - CACHE_EXPIRY_MILLIS
        movieDao.deleteOldMovies(cutoffTime)
        genreDao.deleteOldGenres(cutoffTime)
    }

    private fun shouldRefreshCache(): Boolean {
        return true
    }

    private fun shouldRefreshGenreCache(): Boolean {
        return true
    }
}
