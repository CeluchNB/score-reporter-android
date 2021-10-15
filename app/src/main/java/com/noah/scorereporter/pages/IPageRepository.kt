package com.noah.scorereporter.pages

import com.noah.scorereporter.data.model.Follower
import com.noah.scorereporter.data.model.Season
import com.noah.scorereporter.data.model.Team
import com.noah.scorereporter.pages.model.TeamFollower
import kotlinx.coroutines.flow.Flow

interface IPageRepository {
    suspend fun getTeamById(id: String): Flow<Team>

    suspend fun followTeam(id: String): Flow<Team>

    suspend fun getSeasonsOfTeam(ids: List<String>): Flow<List<Season>>

    suspend fun getFollowersOfTeam(followers: List<Follower>): Flow<List<TeamFollower>>
}