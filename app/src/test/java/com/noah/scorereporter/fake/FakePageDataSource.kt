package com.noah.scorereporter.fake

import com.noah.scorereporter.TestConstants
import com.noah.scorereporter.data.model.Season
import com.noah.scorereporter.data.model.Team
import com.noah.scorereporter.data.model.UserProfile
import com.noah.scorereporter.data.network.PageDataSource
import com.noah.scorereporter.data.network.Result

class FakePageDataSource : PageDataSource {
    var valid = true
    override suspend fun getTeamById(id: String): Result<Team> {
        return if (valid) {
            Result.Success(TestConstants.TEAM_RESPONSE)
        } else {
            Result.Error(Exception(TestConstants.TEAM_ERROR))
        }
    }

    override suspend fun followTeam(id: String): Result<Team> {
        return if (valid) {
            Result.Success(TestConstants.TEAM_RESPONSE)
        } else {
            Result.Error(Exception(TestConstants.TEAM_ERROR))
        }
    }

    override suspend fun getSeasonById(id: String): Result<Season> {
        return if (valid) {
            when (id) {
                TestConstants.SEASON_RESPONSE.id -> Result.Success(TestConstants.SEASON_RESPONSE)
                TestConstants.SEASON_RESPONSE_2.id -> Result.Success(TestConstants.SEASON_RESPONSE_2)
                else -> Result.Error(Exception(TestConstants.SEASON_ERROR))
            }
        } else {
            Result.Error(Exception(TestConstants.SEASON_ERROR))
        }
    }

    override suspend fun getUserById(id: String): Result<UserProfile> {
        return if (valid) {
            when (id) {
                TestConstants.USER_PROFILE_1.id -> Result.Success(TestConstants.USER_PROFILE_1)
                TestConstants.USER_PROFILE_2.id -> Result.Success(TestConstants.USER_PROFILE_2)
                else -> Result.Error(Exception(TestConstants.USER_ERROR))
            }
        } else {
            Result.Error(Exception(TestConstants.USER_ERROR))
        }
    }
}