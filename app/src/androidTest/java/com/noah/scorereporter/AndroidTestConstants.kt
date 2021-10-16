package com.noah.scorereporter

import com.noah.scorereporter.data.model.Role
import com.noah.scorereporter.data.model.UserProfile
import com.noah.scorereporter.data.model.User
import com.noah.scorereporter.data.model.UserTeam

object AndroidTestConstants {

    val USER_RESPONSE = User(
        UserProfile(
            "id",
            "Bob",
            "Test",
            "email@email.com",
            listOf(
                UserTeam("team1", Role.PLAYER),
                UserTeam("team2", Role.COACH)
            )
        ),
        "jwt1"
    )

    val USER_PROFILE = USER_RESPONSE.user

    const val LOGIN_ERROR = "Invalid email or password"
}