package com.noah.scorereporter.model

data class UserProfile (
    val firstName: String,
    val lastName: String,
    val email: String,
    val teams: Map<String, String>
)