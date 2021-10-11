package com.noah.scorereporter.pages

import com.noah.scorereporter.data.model.Team
import kotlinx.coroutines.flow.Flow

interface IPageRepository {
    suspend fun getTeamById(id: String): Flow<Team>

    suspend fun followTeam(id: String): Flow<Team>
}