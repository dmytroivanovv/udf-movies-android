package com.dmytroivanovv.core.movieUseCase

import com.dmytroivanovv.core.Constants
import com.dmytroivanovv.core.favoriteMoviesRepository.FavoriteMoviesRepository
import com.dmytroivanovv.core.movieRepository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject
import javax.inject.Named

class GetMoviesUseCase @Inject constructor(
    private val movieRepository: MoviesRepository,
    private val favoriteMoviesRepository: FavoriteMoviesRepository,
    @Named(Constants.CONTENT_STATIC_URL_PROVISION_NAME) private val staticUrl: String
) {

    suspend fun getMovies(): Flow<Result<List<MovieDomainModel>>> {
        return combine(
            movieRepository.getMovies(),
            favoriteMoviesRepository.favoriteMoviesIds
        ) { movies, favoriteIds ->
            movies.map { list ->
                list.map { movie ->
                    MovieDomainModel(
                        id = movie.id,
                        title = movie.title,
                        year = movie.year,
                        imageUrl = "${staticUrl}${movie.imageName}",
                        description = movie.description,
                        favorite = favoriteIds.contains(movie.id)
                    )
                }
            }
        }
    }
}