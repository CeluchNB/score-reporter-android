package com.noah.scorereporter.pages

import androidx.lifecycle.LiveData
import com.noah.scorereporter.data.model.Team
import com.noah.scorereporter.data.network.Result

interface IPageRepository {
    suspend fun getTeamById(id: String): Result<Team>

    suspend fun followTeam(id: String): Result<Team>
}