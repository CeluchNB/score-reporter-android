package com.noah.scorereporter.pages

import android.util.Log
import com.noah.scorereporter.data.model.Season
import com.noah.scorereporter.data.model.Team
import com.noah.scorereporter.data.model.UserProfile
import com.noah.scorereporter.data.network.PageDataSource
import com.noah.scorereporter.data.network.PageService
import com.noah.scorereporter.data.network.Result
import retrofit2.awaitResponse
import javax.inject.Inject
import kotlin.Exception

class PageDataSourceImpl @Inject constructor(): PageDataSource {

    @Inject
    lateinit var service: PageService

    override suspend fun getTeamById(id: String): Result<Team> {
         val response = service.getTeamById(id).awaitResponse()
        if (response.isSuccessful) {
            response.body()?.let {
                return Result.Success(it)
            }
        }
        return Result.Error(Exception(response.message()))
    }

    override suspend fun followTeam(id: String): Result<Team> {
        val response = service.followTeam(id).awaitResponse()

        if (response.isSuccessful) {
            response.body()?.let {
                return Result.Success(it)
            }
        }
        return Result.Error(Exception(response.message()))
    }

    override suspend fun getSeasonById(id: String): Result<Season> {
        val response = service.getSeasonById(id).awaitResponse()

        if (response.isSuccessful) {
            response.body()?.let {
                return Result.Success(it)
            }
        }

        return Result.Error(Exception(response.message()))
    }

    override suspend fun getUserById(id: String): Result<UserProfile> {
        val response = service.getUserById(id).awaitResponse()

        if (response.isSuccessful) {
            response.body()?.let {
                return Result.Success(it)
            }
        }

        return Result.Error(Exception(response.message()))
    }
}