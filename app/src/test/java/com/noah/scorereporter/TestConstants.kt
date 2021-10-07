package com.noah.scorereporter

import com.noah.scorereporter.data.model.Team
import com.noah.scorereporter.model.UserProfile
import com.noah.scorereporter.data.model.User
import java.time.Instant
import java.util.*

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

    const val LOGIN_ERROR = "Invalid email or password"

    val TEAM_RESPONSE = Team(
        "team_id",
        "LA Dodgers",
        Date(970891721000L),
        Date(1633579721000L),
        "user_1",
        listOf(),
        listOf()
    )

    const val TEAM_ERROR = "Unable to find team"
}