package com.noah.scorereporter.fake

import com.noah.scorereporter.TestConstants
import com.noah.scorereporter.data.model.Team
import com.noah.scorereporter.data.network.Result
import com.noah.scorereporter.pages.IPageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

class FakePageRepository : IPageRepository {
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
}