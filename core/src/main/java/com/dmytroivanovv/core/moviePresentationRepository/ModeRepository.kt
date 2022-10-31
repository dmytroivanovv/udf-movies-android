package com.dmytroivanovv.core.moviePresentationRepository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

interface MovieVisualPresentationTypeRepository {

    val type: StateFlow<MoviePresentationType>

    suspend fun set(newType: MoviePresentationType)
}

class InMemoryMovieVisualPresentationTypeRepositoryImpl @Inject constructor() : MovieVisualPresentationTypeRepository {

    override val type = MutableStateFlow(MoviePresentationType.GRID)

    override suspend fun set(newType: MoviePresentationType) {
        type.emit(newType)
    }
}