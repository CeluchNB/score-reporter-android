package com.noah.scorereporter

import com.noah.scorereporter.network.model.User
import com.noah.scorereporter.network.model.asUserProfile

object Constants {

    val USER_RESPONSE = User(
        "Bob",
        "Test",
        "email@email.com",
        mapOf("team1" to "Player", "team2" to "Coach"),
        "jwt1"
    )

    val USER_PROFILE = USER_RESPONSE.asUserProfile()

    val LOGIN_ERROR = "Invalid email or password"
}