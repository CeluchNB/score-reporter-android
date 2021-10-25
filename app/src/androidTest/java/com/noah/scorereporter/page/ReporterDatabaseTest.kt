package com.noah.scorereporter.page

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.noah.scorereporter.data.local.*
import com.noah.scorereporter.data.model.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.core.IsNull
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ReporterDatabaseTest {

    private lateinit var teamDao: TeamDao
    private lateinit var seasonDao: SeasonDao
    private lateinit var userDao: UserDao
    private lateinit var gameDao: GameDao
    private lateinit var database: ReporterDatabase

    private lateinit var team1: Team
    private lateinit var team2: Team
    private lateinit var season1: Season
    private lateinit var season2: Season
    private lateinit var user1: UserProfile
    private lateinit var user2: UserProfile
    private lateinit var game1: Game
    private lateinit var game2: Game

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ReporterDatabase::class.java
        ).build()

        teamDao = database.teamDao()
        seasonDao = database.seasonDao()
        userDao = database.userDao()
        gameDao = database.gameDao()

        team1 = Team(
            "0",
            "LA Dodgers",
            Date(970891721000L),
            Date(1633579721000L),
            "user_1",
            listOf(),
            listOf()
        )

        team2 = Team(
            "1",
            "Philadelphia Phillies",
            Date(980891721000L),
            Date(1633579821000L),
            "user_2",
            listOf(),
            listOf()
        )

        season1 = Season(
            "0",
            Date(970891721000L),
            Date(1633579721000L),
            "user_1",
            listOf()
        )

        season2 = Season(
            "1",
            Date(980891721000L),
            Date(1633579821000L),
            "user_2",
            listOf()
        )

        user1 = UserProfile(
            "0",
            "First1",
            "Last1",
            "first1@gmail.com",
            listOf()
        )

        user2 = UserProfile(
            "1",
            "First2",
            "Last2",
            "first2@gmail.com",
            listOf()
        )

        game1 = Game(
            "0",
            "0",
            "0",
            "1",
            GameInnings(listOf(), listOf()),
            "0",
            Date(980891721000L)
        )

        game2 = Game(
            "1",
            "1",
            "1",
            "0",
            GameInnings(listOf(), listOf()),
            "1",
            Date(1633579821000L)
        )
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun testSaveTeamAndGetTeamById() = runBlocking {
        teamDao.save(team1, team2)

        val result1 = teamDao.getTeamById(team1.id).take(1).toList()
        val result2 = teamDao.getTeamById(team2.id).take(1).toList()
        val result3 = teamDao.getTeamById("badid").take(1).toList()

        assertThat(result1[0], `is`(team1))
        assertThat(result2[0], `is`(team2))
        assertThat(result3[0], IsNull())
    }

    @Test
    fun testSaveTeamAndHasTeam() = runBlocking {
        teamDao.save(team1, team2)

        val result1 = teamDao.hasTeam(team1.id)
        val result2 = teamDao.hasTeam(team2.id)
        val result3 = teamDao.hasTeam("badid")

        assertThat(result1, `is`(true))
        assertThat(result2, `is`(true))
        assertThat(result3, `is`(false))
    }

    @Test
    fun testSaveSeasonAndGetSeasonById() = runBlocking {
        seasonDao.save(season1, season2)
        val result1 = seasonDao.getSeasonById(season1.id).take(1).toList()
        val result2 = seasonDao.getSeasonById(season2.id).take(1).toList()
        val result3 = seasonDao.getSeasonById("badid").take(1).toList()

        assertThat(result1[0], `is`(season1))
        assertThat(result2[0], `is`(season2))
        assertThat(result3[0], IsNull())
    }

    @Test
    fun testSaveSeasonAndHasSeason() = runBlocking {
        seasonDao.save(season1, season2)

        val result1 = seasonDao.hasSeason(season1.id)
        val result2 = seasonDao.hasSeason(season2.id)
        val result3 = seasonDao.hasSeason("2")

        assertThat(result1, `is`(true))
        assertThat(result2, `is`(true))
        assertThat(result3, `is`(false))
    }

    @Test
    fun testSaveAndGetUserById() = runBlocking {
        userDao.save(listOf(user1, user2))

        val result1 = userDao.getUserById(user1.id).take(1).toList()
        val result2 = userDao.getUserById(user2.id).take(1).toList()
        val result3 = userDao.getUserById("badid").take(1).toList()

        assertThat(result1[0], `is`(user1))
        assertThat(result2[0], `is`(user2))
        assertThat(result3[0], IsNull())
    }

    @Test
    fun testSaveAndHasUser() = runBlocking {
        userDao.save(listOf(user1, user2))

        val result1 = userDao.hasUser(user1.id)
        val result2 = userDao.hasUser(user2.id)
        val result3 = userDao.hasUser("2")

        assertThat(result1, `is`(true))
        assertThat(result2, `is`(true))
        assertThat(result3, `is`(false))
    }

    @Test
    fun testSaveAndGetGameById() = runBlocking {
        gameDao.save(game1, game2)

        val result1 = gameDao.getGameById(game1.id).take(1).toList()
        val result2 = gameDao.getGameById(game2.id).take(1).toList()
        val result3 = gameDao.getGameById("badid").take(1).toList()

        assertThat(result1[0], `is`(game1))
        assertThat(result2[0], `is`(game2))
        assertThat(result3[0], IsNull())
    }

    @Test
    fun testSaveAndHasGame() = runBlocking {
        gameDao.save(game1, game2)

        val result1 = gameDao.hasGame(game1.id)
        val result2 = gameDao.hasGame(game2.id)
        val result3 = gameDao.hasGame("badid")

        assertThat(result1, `is`(true))
        assertThat(result2, `is`(true))
        assertThat(result3, `is`(false))
    }
}