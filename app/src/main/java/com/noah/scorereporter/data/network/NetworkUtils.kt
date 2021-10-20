package com.noah.scorereporter.data.network

class UserNetworkError(
    message: String,
    cause: Throwable?
) : Throwable(message, cause)

class PageNetworkError(
    message: String,
    cause: Throwable?
) : Throwable(message, cause)