package com.noah.scorereporter.fake

import com.noah.scorereporter.TestConstants
import com.noah.scorereporter.data.model.Season
import com.noah.scorereporter.data.model.Team
import com.noah.scorereporter.data.model.UserProfile
import com.noah.scorereporter.data.network.PageDataSource
import com.noah.scorereporter.data.network.PageNetworkError

class FakePageDataSource : PageDataSource {
    var valid = true
    override suspend fun getTeamById(id: String): Team {
        return if (valid) {
            TestConstants.TEAM_RESPONSE
        } else {
            throw PageNetworkError(TestConstants.TEAM_ERROR, Throwable())
        }
    }

    override suspend fun followTeam(jwt: String, id: String): Team {
        return if (valid) {
            TestConstants.TEAM_RESPONSE
        } else {
            throw PageNetworkError(TestConstants.TEAM_ERROR, Throwable())
        }
    }

    override suspend fun getSeasonById(id: String): Season {
        return if (valid) {
            when (id) {
                TestConstants.SEASON_RESPONSE.id -> TestConstants.SEASON_RESPONSE
                TestConstants.SEASON_RESPONSE_2.id -> TestConstants.SEASON_RESPONSE_2
                else -> throw PageNetworkError(TestConstants.SEASON_ERROR, Throwable())
            }
        } else {
            throw PageNetworkError(TestConstants.SEASON_ERROR, Throwable())
        }
    }

    override suspend fun getUserById(id: String): UserProfile {
        return if (valid) {
            when (id) {
                TestConstants.USER_PROFILE_1.id -> TestConstants.USER_PROFILE_1
                TestConstants.USER_PROFILE_2.id -> TestConstants.USER_PROFILE_2
                else -> throw PageNetworkError(TestConstants.USER_ERROR, Throwable())
            }
        } else {
            throw PageNetworkError(TestConstants.USER_ERROR, Throwable())
        }
    }
}