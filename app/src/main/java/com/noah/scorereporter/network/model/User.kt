package com.noah.scorereporter.network.model

import com.noah.scorereporter.model.UserProfile

data class User (
    val firstName: String,
    val lastName: String,
    val email: String,
    val teams: Map<String, String>,
    val jwt: String
)

fun User.asUserProfile() : UserProfile {
    return UserProfile(
        firstName, lastName, email, teams
    )
}