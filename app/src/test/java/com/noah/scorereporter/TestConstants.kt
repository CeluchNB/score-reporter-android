package com.noah.scorereporter

import com.noah.scorereporter.data.model.Season
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

    val SEASON_RESPONSE = Season(
        "season_id_1",
        Date(970891721000L),
        Date(1633579721000L),
        "user_1"
    )

    val SEASON_RESPONSE_2 = Season(
        "season_id_2",
        Date(972891761000L),
        Date(1631579921000L),
        "user_2"
    )

    const val SEASON_ERROR = "Unable to find season"

    val TEAM_RESPONSE = Team(
        "team_id",
        "LA Dodgers",
        Date(970891721000L),
        Date(1633579721000L),
        "user_1",
        listOf(),
        listOf(SEASON_RESPONSE.id, SEASON_RESPONSE_2.id)
    )

    const val TEAM_ERROR = "Unable to find team"
}