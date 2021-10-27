package com.noah.scorereporter

import com.noah.scorereporter.data.model.*
import com.noah.scorereporter.data.model.Follower
import java.util.*

object TestConstants {

    const val LIVE_DATA_ERROR = "LiveData value was never set."

    val USER_RESPONSE_1 = User(
        UserProfile(
            "id1",
            "Bob",
            "Test",
            "email@email.com",
            listOf(
                UserTeam("team_1", Role.COACH),
                UserTeam("team_2", Role.PLAYER)
            )
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
            listOf(
                UserTeam("team_2", Role.COACH)
            )
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
        "user_1",
        listOf()
    )

    val SEASON_RESPONSE_2 = Season(
        "season_id_2",
        Date(972891761000L),
        Date(1631579921000L),
        "user_2",
        listOf()
    )

    const val SEASON_ERROR = "Unable to find season"

    val TEAM_RESPONSE_1 = Team(
        "team_1",
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

    val TEAM_RESPONSE_2 = Team(
        "team_2",
        "Atlanta Braves",
        Date(970891721000L),
        Date(1633579721000L),
        "user_2",
        listOf(
            TeamFollower(USER_PROFILE_2.id, Role.COACH),
            TeamFollower(USER_PROFILE_1.id, Role.PLAYER)
        ),
        listOf(
            TeamSeason("season_1", SEASON_RESPONSE.id)
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

    val GAME_1 = Game(
        "game_1",
        SEASON_RESPONSE.id,
        "team_1",
        "team_2",
        GameInnings(listOf(1,2,1), listOf(1,2,2)),
        "team_2",
        Date(970891721000L)
    )

    val GAME_2 = Game(
        "game_2",
        SEASON_RESPONSE_2.id,
        "team_2",
        "team_1",
        GameInnings(listOf(4,2,2), listOf(2,1,0)),
        "team_2",
        Date(970891721000L)
    )

    val GAME_LIST_ITEM_1 = GameListItem(
        TEAM_RESPONSE_1,
        TEAM_RESPONSE_2,
        4,
        5,
        TeamStatus.HOME
    )

    val GAME_LIST_ITEM_2 = GameListItem(
        TEAM_RESPONSE_2,
        TEAM_RESPONSE_1,
        8,
        3,
        TeamStatus.AWAY
    )

    val GAME_ITEM_1 = GameItem(
        TEAM_RESPONSE_1,
        TEAM_RESPONSE_2,
        SEASON_RESPONSE,
        GAME_1.innings,
        TeamStatus.HOME,
        GAME_1.date
    )

    val GAME_ITEM_2 = GameItem(
        TEAM_RESPONSE_2,
        TEAM_RESPONSE_1,
        SEASON_RESPONSE_2,
        GAME_2.innings,
        TeamStatus.AWAY,
        GAME_2.date
    )

    const val GAME_ERROR = "Unable to find game"
}