package com.noah.scorereporter

import com.noah.scorereporter.model.UserProfile

object Constants {
    val USER_PROFILE = UserProfile(
        "Bob",
        "Test",
        "email@email.com",
        mapOf("team1" to "Player", "team2" to "Coach")
    )
}