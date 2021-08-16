package com.gmail.maystruks08.domain.dispatchers

import kotlinx.coroutines.CoroutineDispatcher

interface CoroutineDispatchers {
    fun ui(): CoroutineDispatcher
    fun io(): CoroutineDispatcher
    fun default(): CoroutineDispatcher
}
