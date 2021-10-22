package com.noah.scorereporter.data.network

import com.noah.scorereporter.data.model.Season
import com.noah.scorereporter.data.model.Team
import com.noah.scorereporter.data.model.UserProfile

interface PageDataSource {
    suspend fun getTeamById(id: String): Team

    suspend fun followTeam(jwt: String, id: String): Team

    suspend fun getSeasonById(id: String): Season

    suspend fun getUserById(id: String): UserProfile
}