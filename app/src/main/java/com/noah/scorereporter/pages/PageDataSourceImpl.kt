package com.noah.scorereporter.pages

import com.noah.scorereporter.data.model.Team
import com.noah.scorereporter.data.network.PageDataSource
import com.noah.scorereporter.data.network.PageService
import com.noah.scorereporter.data.network.Result
import retrofit2.awaitResponse
import java.lang.Exception
import javax.inject.Inject

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
}