package com.noah.scorereporter.pages

import com.noah.scorereporter.data.model.*
import kotlinx.coroutines.flow.Flow

interface IPageRepository {
    suspend fun getTeamById(id: String): Flow<Team>

    suspend fun followTeam(id: String): Flow<Team>

    suspend fun getSeasonsOfTeam(ids: List<String>): Flow<List<Season>>

    suspend fun getFollowersOfTeam(teamFollowers: List<TeamFollower>): Flow<List<Follower>>

    suspend fun canFollow(id: String): Boolean

    suspend fun getSeasonById(id: String): Flow<Season>

    suspend fun getGamesOfSeason(ids: List<String>): Flow<List<Game>>
}