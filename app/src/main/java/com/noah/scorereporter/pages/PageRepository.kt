package com.noah.scorereporter.pages

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.noah.scorereporter.data.local.TeamDao
import com.noah.scorereporter.data.model.Team
import com.noah.scorereporter.data.network.PageDataSource
import com.noah.scorereporter.data.network.Result
import com.noah.scorereporter.data.network.succeeded
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PageRepository @Inject
constructor(
    private val remoteDataSource: PageDataSource,
    private val teamDao: TeamDao
) : IPageRepository {

    override suspend fun getTeamById(id: String): Result<Team> {
        if (!teamDao.hasTeam(id)) {
            val result = remoteDataSource.getTeamById(id)
            if (result.succeeded) {
                result as Result.Success
                teamDao.save(listOf(result.data))
            }
        }

        return try {
            val team = teamDao.getTeamById(id)
            team?.let {
                return Result.Success(it)
            }
            return Result.Error(Exception("No team found"))
        } catch (exception: Exception) {
            Result.Error(exception)
        }
    }

    override suspend fun followTeam(id: String): Result<Team> {
        val result = remoteDataSource.followTeam(id)
        if (result.succeeded) {
            result as Result.Success
            teamDao.save(listOf(result.data))
        }

        return try {
            val team = teamDao.getTeamById(id)
            team?.let {
                return Result.Success(it)
            }
            return Result.Error(Exception("No team found"))
        } catch (exception: Exception) {
            Result.Error(exception)
        }
    }
}