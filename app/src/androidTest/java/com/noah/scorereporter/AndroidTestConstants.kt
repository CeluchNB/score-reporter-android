package com.noah.scorereporter

import com.noah.scorereporter.data.model.UserProfile
import com.noah.scorereporter.data.model.User

object AndroidTestConstants {

    val USER_RESPONSE = User(
        UserProfile(
            "id",
            "Bob",
            "Test",
            "email@email.com",
            mapOf("team1" to "Player", "team2" to "Coach")
        ),
        "jwt1"
    )

    val USER_PROFILE = USER_RESPONSE.user

    const val LOGIN_ERROR = "Invalid email or password"
}