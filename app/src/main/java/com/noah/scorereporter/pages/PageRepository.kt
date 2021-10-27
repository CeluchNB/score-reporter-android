package com.noah.scorereporter.pages

import android.content.SharedPreferences
import android.util.Log
import com.noah.scorereporter.data.local.GameDao
import com.noah.scorereporter.data.local.SeasonDao
import com.noah.scorereporter.data.local.TeamDao
import com.noah.scorereporter.data.local.UserDao
import com.noah.scorereporter.data.model.*
import com.noah.scorereporter.data.network.PageDataSource
import com.noah.scorereporter.data.network.PageNetworkError
import com.noah.scorereporter.data.network.UserDataSource
import com.noah.scorereporter.data.network.UserNetworkError
import com.noah.scorereporter.util.Constants
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class PageRepository @Inject
constructor(
    private val pageDataSource: PageDataSource,
    private val userDataSource: UserDataSource,
    private val sharedPrefs: SharedPreferences,
    private val teamDao: TeamDao,
    private val seasonDao: SeasonDao,
    private val userDao: UserDao,
    private val gameDao: GameDao
) : IPageRepository {

    override suspend fun getTeamById(id: String): Flow<Team> {
        if (!teamDao.hasTeam(id)) {
            try {
                val result = pageDataSource.getTeamById(id)
                teamDao.save(result)
            } catch (exception: PageNetworkError) {
                throw exception
            }
        }

        return teamDao.getTeamById(id)
    }

    override suspend fun followTeam(id: String): Flow<Team> {
        try {
            val jwt = sharedPrefs.getString(Constants.USER_TOKEN, "")
            val result = pageDataSource.followTeam(jwt ?: "", id)
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
                            val result = pageDataSource.getSeasonById(id)
                            newSeasons.add(result)
                        } catch (exception: PageNetworkError) {
                            Log.e("PageRepository", exception.message ?: "")
                        }
                    }
                }
            }
        }

        seasonDao.save(*newSeasons.toTypedArray())

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
                            val result = pageDataSource.getUserById(it.user)
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

    override suspend fun canFollow(id: String): Boolean {
        val jwt = sharedPrefs.getString(Constants.USER_TOKEN, "")

        if (jwt.isNullOrBlank()) {
            return false
        }

        // val teams = userDao.getUserById()
        var user: UserProfile? = null
        coroutineScope {
            launch {
                user = try {
                    userDataSource.getProfile(jwt)
                } catch (exception: UserNetworkError) {
                    null
                }
            }
        }

        if (user == null) {
            return false
        }

        user?.teams?.forEach { team ->
            if (team.team == id) {
                return false
            }
        }

        return true
    }

    override suspend fun getSeasonById(id: String): Flow<Season> {
        if (!seasonDao.hasSeason(id)) {
            try {
                val result = pageDataSource.getSeasonById(id)
                seasonDao.save(result)
            } catch (exception: PageNetworkError) {
                throw exception
            }
        }
        return seasonDao.getSeasonById(id)
    }

    override suspend fun getGamesOfSeason(ids: List<String>): Flow<List<Game>> {
        val newGames = mutableListOf<Game>()

        coroutineScope {
            ids.forEach {
                if (!gameDao.hasGame(it)) {
                    launch {
                        try {
                            val game = pageDataSource.getGameById(it)
                            newGames.add(game)
                        } catch (exception: PageNetworkError) {
                            Log.e("PageRepository", exception.message ?: "")
                        }
                    }
                }
            }
        }

        gameDao.save(*newGames.toTypedArray())

        val games = mutableListOf<Game>()
        ids.forEach {
            gameDao.getGameById(it).take(1).collect { game ->
                games.add(game)
            }
        }
        return flow { emit(games) }
    }

    override suspend fun getGameListItems(games: List<Game>): Flow<List<GameListItem>> {
        val newTeams = mutableListOf<Team>()

        coroutineScope {
            games.forEach { game ->
                if (!teamDao.hasTeam(game.awayTeam)) {
                    launch {
                        try {
                            val team = pageDataSource.getTeamById(game.awayTeam)
                            newTeams.add(team)
                        } catch (exception: PageNetworkError) {
                            Log.e("PageRepository", exception.message ?: "")
                        }
                    }
                }

                if (!teamDao.hasTeam(game.homeTeam)) {
                    launch {
                        try {
                            val team = pageDataSource.getTeamById(game.homeTeam)
                            newTeams.add(team)
                        } catch (exception: PageNetworkError) {
                            Log.e("PageRepository", exception.message ?: "")
                        }
                    }
                }
            }
        }

        teamDao.save(*newTeams.toTypedArray())

        val listItems = mutableListOf<GameListItem>()

        games.forEach { game ->
            if (teamDao.hasTeam(game.awayTeam) && teamDao.hasTeam(game.homeTeam)) {
                val awayTeam = teamDao.getTeamById(game.awayTeam)
                val homeTeam = teamDao.getTeamById(game.homeTeam)

                lateinit var away: Team
                lateinit var home: Team

                awayTeam.take(1).collect { away = it }
                homeTeam.take(1).collect { home = it }

                val gameListItem = GameListItem(
                    away,
                    home,
                    game.innings.away.sum(),
                    game.innings.home.sum(),
                    if (game.winner == away.id) TeamStatus.AWAY else TeamStatus.HOME
                )

                listItems.add(gameListItem)
            }
        }

        return flow { emit(listItems) }
    }

    override suspend fun getGame(id: String): Flow<GameItem> {
        TODO("Not yet implemented")
    }
}