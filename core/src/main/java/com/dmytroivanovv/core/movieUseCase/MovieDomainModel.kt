package com.dmytroivanovv.core.movieUseCase

data class MovieDomainModel(
    val id: String,
    val title: String,
    val year: Int,
    val imageUrl: String,
    val description: String,
    val favorite: Boolean
)