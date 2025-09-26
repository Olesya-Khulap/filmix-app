package com.example.filmix.di

import com.example.filmix.data.repository.MovieRepositoryImpl
import com.example.filmix.data.repository.SearchRepositoryImpl
import com.example.filmix.data.repository.TvShowRepositoryImpl
import com.example.filmix.data.repository.UserListRepositoryImpl
import com.example.filmix.domain.repository.MovieRepository
import com.example.filmix.domain.repository.SearchRepository
import com.example.filmix.domain.repository.TvShowRepository
import com.example.filmix.domain.repository.UserListRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMovieRepository(
        movieRepositoryImpl: MovieRepositoryImpl
    ): MovieRepository

    @Binds
    @Singleton
    abstract fun bindUserListRepository(
        userListRepositoryImpl: UserListRepositoryImpl
    ): UserListRepository

    @Binds
    abstract fun bindTvShowRepository(
        tvShowRepositoryImpl: TvShowRepositoryImpl
    ): TvShowRepository

    @Binds
    abstract fun bindSearchRepository(
        searchRepositoryImpl: SearchRepositoryImpl
    ): SearchRepository

}
