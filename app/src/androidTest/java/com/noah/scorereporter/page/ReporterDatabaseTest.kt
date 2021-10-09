package com.noah.scorereporter.page

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.noah.scorereporter.data.local.ReporterDatabase
import com.noah.scorereporter.data.local.TeamDao
import com.noah.scorereporter.data.model.Team
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
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
    private lateinit var database: ReporterDatabase

    private lateinit var team1: Team

    private lateinit var team2: Team

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ReporterDatabase::class.java
        ).build()

        teamDao = database.teamDao()

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
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun testSaveAndGetTeamById() = runBlocking {
        val job1 = async { teamDao.getTeamById("0").take(1).toList() }
        val job2 = async { teamDao.getTeamById("1").take(1).toList() }
        val job3 = async { teamDao.getTeamById("2").take(1).toList() }

        teamDao.save(listOf(team1, team2))

        val result1 = job1.await()
        val result2 = job2.await()
        val result3 = job3.await()

        assertThat(result1[0], `is`(team1))
        assertThat(result2[0], `is`(team2))
        assertThat(result3[0], IsNull())
    }

    @Test
    fun testSaveAndHasTeam() {
        teamDao.save(listOf(team1, team2))

        val result1 = teamDao.hasTeam("0")
        val result2 = teamDao.hasTeam("1")
        val result3 = teamDao.hasTeam("2")

        assertThat(result1, `is`(true))
        assertThat(result2, `is`(true))
        assertThat(result3, `is`(false))
    }
}