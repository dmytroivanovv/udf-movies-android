package com.dmytroivanovv.core

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface CoroutineDispatchers {

    val io: CoroutineDispatcher
        get() = Dispatchers.IO

    val ui: CoroutineDispatcher
        get() = Dispatchers.Main

    val default: CoroutineDispatcher
        get() = Dispatchers.Default
}