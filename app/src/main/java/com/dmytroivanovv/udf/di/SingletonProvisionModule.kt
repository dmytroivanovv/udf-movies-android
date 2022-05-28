package com.dmytroivanovv.udf.di

import com.dmytroivanovv.core.CoroutineDispatchers
import com.dmytroivanovv.core.Constants
import com.dmytroivanovv.core.movieRepository.MovieNetworkDataSourceService
import com.dmytroivanovv.core.movieRepository.MoviesRepository
import com.dmytroivanovv.core.movieRepository.MoviesRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class SingletonProvisionModule {

    @Provides
    @Singleton
    fun provideCoroutineDispatchers(): CoroutineDispatchers {
        return object : CoroutineDispatchers { }
    }

    @Provides
    @Named(Constants.CONTENT_STATIC_URL_PROVISION_NAME)
    @Singleton
    fun provideContentStaticUrl(): String {
        return Constants.CONTENT_STATIC_URL
    }

    @Provides
    @Singleton
    fun provideMoviesRepository(
        coroutineDispatchers: CoroutineDispatchers
    ): MoviesRepository {
        val service = Retrofit.Builder()
            .baseUrl(Constants.CONTENT_STATIC_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MovieNetworkDataSourceService::class.java)

        return MoviesRepositoryImpl(
            networkService = service,
            coroutineDispatchers = coroutineDispatchers
        )
    }
}