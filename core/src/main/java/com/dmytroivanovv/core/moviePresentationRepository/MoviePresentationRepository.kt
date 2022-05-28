package com.dmytroivanovv.core.moviePresentationRepository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

interface MoviePresentationRepository {

    val type: StateFlow<MoviePresentationType>

    suspend fun set(newType: MoviePresentationType)
}

class InMemoryMoviePresentationRepositoryImpl @Inject constructor() : MoviePresentationRepository {

    override val type = MutableStateFlow(MoviePresentationType.GRID)

    override suspend fun set(newType: MoviePresentationType) {
        type.emit(newType)
    }
}