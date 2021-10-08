package com.noah.scorereporter.pages

import com.noah.scorereporter.data.local.TeamDao
import com.noah.scorereporter.data.model.Team
import com.noah.scorereporter.data.network.PageDataSource
import com.noah.scorereporter.data.network.Result
import com.noah.scorereporter.data.network.succeeded
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PageRepository @Inject
constructor(
    private val remoteDataSource: PageDataSource,
    private val teamDao: TeamDao
) : IPageRepository {

    override suspend fun getTeamById(id: String): Flow<Team> {
        if (!teamDao.hasTeam(id)) {
            val result = remoteDataSource.getTeamById(id)
            if (result.succeeded) {
                result as Result.Success
                teamDao.save(listOf(result.data))
            }
        }

        return teamDao.getTeamById(id)
    }

    override suspend fun followTeam(id: String): Flow<Team> {
        val result = remoteDataSource.followTeam(id)
        if (result.succeeded) {
            result as Result.Success
            teamDao.save(listOf(result.data))
        }

        return teamDao.getTeamById(id)
    }
}