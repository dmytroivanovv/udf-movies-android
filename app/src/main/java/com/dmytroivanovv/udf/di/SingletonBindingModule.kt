package com.dmytroivanovv.udf.di

import com.dmytroivanovv.core.favoriteMoviesRepository.FavoriteMoviesRepository
import com.dmytroivanovv.core.favoriteMoviesRepository.InMemoryFavoriteMoviesRepositoryImpl
import com.dmytroivanovv.core.moviePresentationRepository.ModeRepositoryImpl
import com.dmytroivanovv.core.moviePresentationRepository.ModeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class SingletonBindingModule {

    @Binds
    @Singleton
    abstract fun bindFavoriteMoviesRepository(
        impl: InMemoryFavoriteMoviesRepositoryImpl
    ): FavoriteMoviesRepository

    @Binds
    @Singleton
    abstract fun bindMoviePresentationRepository(
        impl: ModeRepositoryImpl
    ): ModeRepository
}