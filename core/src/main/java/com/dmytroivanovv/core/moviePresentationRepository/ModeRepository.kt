package com.dmytroivanovv.core.moviePresentationRepository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

interface ModeRepository {

    val type: StateFlow<Mode>

    suspend fun set(newType: Mode)
}

class ModeRepositoryImpl @Inject constructor() : ModeRepository {

    override val type = MutableStateFlow(Mode.GRID)

    override suspend fun set(newType: Mode) {
        type.emit(newType)
    }
}