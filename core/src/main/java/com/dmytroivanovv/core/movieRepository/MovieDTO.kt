package com.dmytroivanovv.core.movieRepository

data class MovieDTO(
    val id: String,
    val title: String,
    val year: Int,
    val imageName: String,
    val description: String
)