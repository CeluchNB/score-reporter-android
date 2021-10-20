package com.noah.scorereporter.pages

import android.util.Log
import com.noah.scorereporter.data.local.SeasonDao
import com.noah.scorereporter.data.local.TeamDao
import com.noah.scorereporter.data.local.UserDao
import com.noah.scorereporter.data.model.Season
import com.noah.scorereporter.data.model.Team
import com.noah.scorereporter.data.model.TeamFollower
import com.noah.scorereporter.data.model.UserProfile
import com.noah.scorereporter.data.network.PageDataSource
import com.noah.scorereporter.data.network.PageNetworkError
import com.noah.scorereporter.data.network.Result
import com.noah.scorereporter.data.network.succeeded
import com.noah.scorereporter.pages.model.Follower
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import javax.inject.Inject

class PageRepository @Inject
constructor(
    private val remoteDataSource: PageDataSource,
    private val teamDao: TeamDao,
    private val seasonDao: SeasonDao,
    private val userDao: UserDao
) : IPageRepository {

    override suspend fun getTeamById(id: String): Flow<Team> {
        if (!teamDao.hasTeam(id)) {
            try {
                val result = remoteDataSource.getTeamById(id)
                teamDao.save(result)
            } catch (exception: PageNetworkError) {
                throw exception
            }
        }

        return teamDao.getTeamById(id)
    }

    override suspend fun followTeam(id: String): Flow<Team> {
        try {
            val result = remoteDataSource.followTeam(id)
            teamDao.save(result)
        } catch (exception: PageNetworkError) {
            throw exception
        }

        return teamDao.getTeamById(id)
    }

    override suspend fun getSeasonsOfTeam(ids: List<String>): Flow<List<Season>> {
        val newSeasons = mutableListOf<Season>()
        // get all seasons we don't already have yet
        coroutineScope {
            ids.forEach { id ->
                launch {
                    if (!seasonDao.hasSeason(id)) {
                        try {
                            val result = remoteDataSource.getSeasonById(id)
                            newSeasons.add(result)
                        } catch (exception: PageNetworkError) {
                            Log.e("Noah", exception.message ?: "")
                        }
                    }
                }
            }
        }

        seasonDao.save(newSeasons)

        val list = mutableListOf<Season>()
        ids.forEach { id ->
            if (seasonDao.hasSeason(id)) {
                seasonDao.getSeasonById(id).take(1).collect { season ->
                    list.add(season)
                }
            }
        }
        return flow { emit(list) }
    }

    override suspend fun getFollowersOfTeam(teamFollowers: List<TeamFollower>): Flow<List<Follower>> {
        val newFollowers = mutableListOf<UserProfile>()

        coroutineScope {
            teamFollowers.forEach {
                launch {
                    if (!userDao.hasUser(it.user)) {
                        try {
                            val result = remoteDataSource.getUserById(it.user)
                            newFollowers.add(result)
                        } catch (exception: PageNetworkError) {
                            Log.e("PageRepository", exception.message ?: "")
                        }
                    }
                }
            }
        }

        userDao.save(newFollowers)

        val followers = mutableListOf<Follower>()
        teamFollowers.forEach { follower ->
            if (userDao.hasUser(follower.user)) {
                userDao.getUserById(follower.user).take(1).collect { user ->
                    followers.add(
                        Follower(user.firstName, user.lastName, user.email, follower.role)
                    )
                }
            }
        }

        return flow { emit(followers) }
    }
}