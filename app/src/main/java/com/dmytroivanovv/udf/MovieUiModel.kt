package com.dmytroivanovv.udf

data class MovieUiModel(
    val id: String,
    val title: String,
    val year: String,
    val imageUrl: String,
    val description: String,
    val favorite: Boolean,
)
