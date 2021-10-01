package com.noah.scorereporter.util

open class Event <out T>(private val content: T) {

    var handled = false
        private set

    fun getContentIfNotHandled(): T? {
        return if (!handled) {
            handled = true
            content
        } else {
            null
        }
    }

    fun peekContent(): T = content
}