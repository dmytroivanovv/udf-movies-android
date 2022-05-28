package com.dmytroivanovv.core.favoriteMoviesRepository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

interface FavoriteMoviesRepository {

    val favoriteMoviesIds: Flow<Set<String>>

    suspend fun add(id: String)

    suspend fun remove(id: String)
}

class InMemoryFavoriteMoviesRepositoryImpl @Inject constructor() : FavoriteMoviesRepository {

    override val favoriteMoviesIds: MutableStateFlow<Set<String>> = MutableStateFlow(emptySet())

    override suspend fun add(id: String) {
        val set = favoriteMoviesIds.value.toMutableSet()
        set.add(id)
        favoriteMoviesIds.emit(set)
    }

    override suspend fun remove(id: String) {
        val set = favoriteMoviesIds.value.toMutableSet()
        set.remove(id)
        favoriteMoviesIds.emit(set)
    }
}