package com.noah.scorereporter.fake

import com.noah.scorereporter.TestConstants
import com.noah.scorereporter.data.model.Team
import com.noah.scorereporter.pages.IPageRepository
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
}