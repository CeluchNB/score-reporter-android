package com.noah.scorereporter.fake

import com.noah.scorereporter.TestConstants
import com.noah.scorereporter.data.model.Season
import com.noah.scorereporter.data.model.Team
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
            Result.Success(TestConstants.SEASON_RESPONSE)
        } else {
            Result.Error(Exception(TestConstants.SEASON_ERROR))
        }
    }
}