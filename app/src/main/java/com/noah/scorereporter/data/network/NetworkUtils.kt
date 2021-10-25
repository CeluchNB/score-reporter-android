package com.noah.scorereporter.data.network

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class UserNetworkError(
    message: String,
    cause: Throwable?
) : Throwable(message, cause)

class PageNetworkError(
    message: String,
    cause: Throwable?
) : Throwable(message, cause)

interface DispatcherProvider {
    fun main() : CoroutineDispatcher = Dispatchers.Main

    fun io() = Dispatchers.IO

    fun default() = Dispatchers.Default

    fun unconfined() = Dispatchers.Unconfined
}

class DefaultDispatcherProvider @Inject constructor() : DispatcherProvider