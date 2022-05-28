package com.dmytroivanovv.core.movieRepository

import com.dmytroivanovv.core.CoroutineDispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface MoviesRepository {

    suspend fun getMovies(): Flow<Result<List<MovieDTO>>>

}

class MoviesRepositoryImpl @Inject constructor(
    private val networkService: MovieNetworkDataSourceService,
    private val coroutineDispatchers: CoroutineDispatchers
): MoviesRepository {

    override suspend fun getMovies(): Flow<Result<List<MovieDTO>>> {
        return flow {
            val result = withContext(coroutineDispatchers.io) {
                try {
                    val movies = networkService.getMovies()
                    Result.success(movies)
                } catch (e: Exception) {
                    Result.failure(e)
                }
            }
            emit(result)
        }
    }

}

