package com.noah.scorereporter

import com.noah.scorereporter.model.UserProfile
import com.noah.scorereporter.network.model.User

object TestConstants {

    val USER_RESPONSE = User(
        UserProfile(
            "Bob",
            "Test",
            "email@email.com",
            mapOf("team1" to "Player", "team2" to "Coach")
        ),
        "jwt1"
    )

    val USER_PROFILE = USER_RESPONSE.user

    val LOGIN_ERROR = "Invalid email or password"
}