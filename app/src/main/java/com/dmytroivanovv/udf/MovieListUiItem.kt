package com.dmytroivanovv.udf

sealed class MovieListUiItem {

    data class LinearMovie(
        val movie: MovieUiModel
    ): MovieListUiItem()

    data class GridMovies(
        val leftMovie: MovieUiModel,
        val rightMovie: MovieUiModel?
    ): MovieListUiItem()

    object Loading : MovieListUiItem()

    data class Error(
        val text: String
    ): MovieListUiItem()

    object Empty : MovieListUiItem()
}