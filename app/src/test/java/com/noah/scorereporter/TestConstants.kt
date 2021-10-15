package com.noah.scorereporter

import com.noah.scorereporter.data.model.*
import com.noah.scorereporter.pages.model.Follower
import java.util.*

object TestConstants {

    const val LIVE_DATA_ERROR = "LiveData value was never set."

    val USER_RESPONSE_1 = User(
        UserProfile(
            "id1",
            "Bob",
            "Test",
            "email@email.com",
            mapOf("team1" to "Player", "team2" to "Coach")
        ),
        "jwt1"
    )

    val USER_PROFILE_1 = USER_RESPONSE_1.user

    val USER_RESPONSE_2 = User(
        UserProfile(
            "id2",
            "First",
            "Last",
            "first@email.com",
            mapOf("team2" to "Player")
        ),
        "jwt2"
    )

    val USER_PROFILE_2 = USER_RESPONSE_2.user

    const val LOGIN_ERROR = "Invalid email or password"

    const val USER_ERROR = "Unable to get user"

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
        listOf(
            TeamFollower(USER_PROFILE_1.id, Role.COACH),
            TeamFollower(USER_PROFILE_2.id, Role.PLAYER)
        ),
        listOf(
            TeamSeason("season_1", SEASON_RESPONSE.id),
            TeamSeason("season_2", SEASON_RESPONSE_2.id)
        )
    )

    const val TEAM_ERROR = "Unable to find team"

    val FOLLOWER_1 = Follower(
        USER_PROFILE_1.firstName,
        USER_PROFILE_1.lastName,
        USER_PROFILE_1.email,
        Role.COACH
    )

    val FOLLOWER_2 = Follower(
        USER_PROFILE_2.firstName,
        USER_PROFILE_2.lastName,
        USER_PROFILE_2.email,
        Role.PLAYER
    )
}