package com.noah.scorereporter.pages

import com.noah.scorereporter.data.model.Season
import com.noah.scorereporter.data.model.Team
import com.noah.scorereporter.data.model.UserProfile
import com.noah.scorereporter.data.network.PageDataSource
import com.noah.scorereporter.data.network.PageNetworkError
import com.noah.scorereporter.data.network.PageService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class PageDataSourceImpl @Inject constructor(): PageDataSource {

    @Inject
    lateinit var service: PageService

    override suspend fun getTeamById(id: String): Team {
        return getServiceResponse("Unable to get team") {
            service.getTeamById(id)
        }
    }

    override suspend fun followTeam(id: String): Team {
        return getServiceResponse("Unable to follow team") {
            service.followTeam(id)
        }
    }

    override suspend fun getSeasonById(id: String): Season {
        return getServiceResponse("Unable to get season") {
            service.getSeasonById(id)
        }
    }

    override suspend fun getUserById(id: String): UserProfile {
        return getServiceResponse("Unable to get user") {
            service.getUserById(id)
        }
    }

    private suspend fun <T> getServiceResponse(message: String, block: suspend () -> T): T {
        return try {
            block()
        } catch (cause: Throwable) {
            throw PageNetworkError(message, cause)
        }
    }
}