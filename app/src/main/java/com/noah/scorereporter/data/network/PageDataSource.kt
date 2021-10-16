package com.noah.scorereporter.data.network

import com.noah.scorereporter.data.model.Season
import com.noah.scorereporter.data.model.Team
import com.noah.scorereporter.data.model.UserProfile

interface PageDataSource {
    suspend fun getTeamById(id: String): Result<Team>

    suspend fun followTeam(id: String): Result<Team>

    suspend fun getSeasonById(id: String): Result<Season>

    suspend fun getUserById(id: String): Result<UserProfile>
}