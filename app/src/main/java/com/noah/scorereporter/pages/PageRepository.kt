package com.noah.scorereporter.pages

import com.noah.scorereporter.data.local.SeasonDao
import com.noah.scorereporter.data.local.TeamDao
import com.noah.scorereporter.data.model.Season
import com.noah.scorereporter.data.model.Team
import com.noah.scorereporter.data.network.PageDataSource
import com.noah.scorereporter.data.network.Result
import com.noah.scorereporter.data.network.succeeded
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class PageRepository @Inject
constructor(
    private val remoteDataSource: PageDataSource,
    private val teamDao: TeamDao,
    private val seasonDao: SeasonDao
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

    override suspend fun getSeasonsOfTeam(ids: List<String>): Flow<List<Season>> {
        val newSeasons = mutableListOf<Season>()
        // get all seasons we don't already have yet
        ids.forEach { id ->
            if (!seasonDao.hasSeason(id)) {
                coroutineScope {
                    val result = remoteDataSource.getSeasonById(id)
                    if (result.succeeded) {
                        newSeasons.add((result as Result.Success).data)
                    }
                }
            }
        }

        seasonDao.save(newSeasons)

        val list = mutableListOf<Season>()
        ids.forEach { id ->
            if (seasonDao.hasSeason(id)) {
                seasonDao.getSeasonById(id).collect { season ->
                    list.add(season)
                }
            }
        }

        return flow { emit(list) }
    }
}