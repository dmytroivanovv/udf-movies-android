package com.dmytroivanovv.udf

import com.dmytroivanovv.core.moviePresentationRepository.Mode
import com.dmytroivanovv.core.movieUseCase.MovieDomainModel

object MovieListViewModelUtil {

    fun mapToUiStates(
        moviesResult: Result<List<MovieDomainModel>>,
        mode: Mode
    ): List<MovieListUiItem> {
        if (moviesResult.isFailure) {
            return listOf(
                MovieListUiItem.Error(text = moviesResult.exceptionOrNull()?.message ?: "")
            )
        }
        val movies = moviesResult.getOrThrow()

        if (movies.isEmpty()) {
            return listOf(MovieListUiItem.Empty)
        }

        return when (mode) {
            Mode.LIST -> mapToListUiStates(movies = movies)
            Mode.GRID -> mapToGridUiStates(movies = movies)
        }
    }

    private fun mapToListUiStates(
        movies: List<MovieDomainModel>
    ): List<MovieListUiItem> {
        return movies.map { movie ->
            MovieListUiItem.ListMovie(
                movie = mapDomainMovieToUiState(movie = movie)
            )
        }
    }

    private fun mapToGridUiStates(
        movies: List<MovieDomainModel>
    ): List<MovieListUiItem> {
        return movies.windowed(size = 2, step = 2, partialWindows = true) { bundle ->
            val movie1 = bundle[0]
            val movie2 = bundle.getOrNull(1)
            MovieListUiItem.GridMovie(
                movie1 = mapDomainMovieToUiState(movie = movie1),
                movie2 = movie2?.let { movie ->
                    mapDomainMovieToUiState(movie = movie)
                }
            )
        }
    }

    private fun mapDomainMovieToUiState(movie: MovieDomainModel): MovieUiModel {
        return MovieUiModel(
            id = movie.id,
            title = movie.title,
            year = movie.year.toString(),
            imageUrl = movie.imageUrl,
            description = movie.description,
            favorite = movie.favorite
        )
    }
}