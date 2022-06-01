package com.dmytroivanovv.udf

sealed class MovieListUiItem {

    data class ListMovie(
        val movie: MovieUiModel
    ): MovieListUiItem()

    data class GridMovie(
        val movie1: MovieUiModel,
        val movie2: MovieUiModel?
    ): MovieListUiItem()

    object Loading : MovieListUiItem()

    data class Error(
        val text: String
    ): MovieListUiItem()

    object Empty : MovieListUiItem()
}