package com.noah.scorereporter.fake

import com.noah.scorereporter.TestConstants
import com.noah.scorereporter.data.model.Role
import com.noah.scorereporter.data.model.Season
import com.noah.scorereporter.data.model.Team
import com.noah.scorereporter.data.model.TeamFollower
import com.noah.scorereporter.pages.IPageRepository
import com.noah.scorereporter.pages.model.Follower
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakePageRepository : IPageRepository {
    var valid = true

    override suspend fun getTeamById(id: String): Flow<Team> {
        return if (valid) {
            flow { emit(TestConstants.TEAM_RESPONSE) }
        } else {
            flow { }
        }
    }

    override suspend fun followTeam(id: String): Flow<Team> {
        return if (valid) {
            flow { emit(TestConstants.TEAM_RESPONSE) }
        } else {
            flow { }
        }
    }

    override suspend fun getSeasonsOfTeam(ids: List<String>): Flow<List<Season>> {
        val list = mutableListOf<Season>()

        if (ids.contains(TestConstants.SEASON_RESPONSE.id)) {
            list.add(TestConstants.SEASON_RESPONSE)
        }

        if (ids.contains(TestConstants.SEASON_RESPONSE_2.id)) {
            list.add(TestConstants.SEASON_RESPONSE_2)
        }

        return if (valid) {
            flow { emit(list) }
        } else {
            flow { }
        }
    }

    override suspend fun getFollowersOfTeam(teamFollowers: List<TeamFollower>): Flow<List<Follower>> {
        val list = mutableListOf<Follower>()

        teamFollowers.forEach {
            if (it.user == TestConstants.USER_PROFILE_1.id) {
                list.add(
                    Follower(
                        TestConstants.USER_PROFILE_1.firstName,
                        TestConstants.USER_PROFILE_1.lastName,
                        TestConstants.USER_PROFILE_1.email,
                        Role.COACH
                    )
                )
            }

            if (it.user == TestConstants.USER_PROFILE_2.id) {
                list.add(
                    Follower(
                        TestConstants.USER_PROFILE_2.firstName,
                        TestConstants.USER_PROFILE_2.lastName,
                        TestConstants.USER_PROFILE_2.email,
                        Role.PLAYER
                    )
                )
            }
        }

        return if (valid) {
            flow { emit(list) }
        } else {
            flow { }
        }
    }

    override suspend fun canFollow(id: String): Boolean {
        return valid
    }
}