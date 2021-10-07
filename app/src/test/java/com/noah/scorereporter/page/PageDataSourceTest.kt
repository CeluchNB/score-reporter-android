package com.noah.scorereporter.page

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.noah.scorereporter.MainCoroutineRule
import com.noah.scorereporter.TestConstants
import com.noah.scorereporter.data.network.PageService
import com.noah.scorereporter.data.network.Result
import com.noah.scorereporter.data.network.succeeded
import com.noah.scorereporter.fake.MockPageClient
import com.noah.scorereporter.pages.PageDataSourceImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class PageDataSourceTest {

    private lateinit var dataSource: PageDataSourceImpl
    private lateinit var validService: PageService
    private lateinit var invalidService: PageService

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        dataSource = PageDataSourceImpl()
        validService = MockPageClient.createValid()
        invalidService = MockPageClient.createInvalid()
    }

    @Test
    fun `test valid getTeamById`() = runBlocking {
        dataSource.service = validService

        val result = dataSource.getTeamById("team_id")
        assertThat(result.succeeded, `is`(true))
        result as Result.Success

        val team = result.data
        assertThat(team.id, `is`(TestConstants.TEAM_RESPONSE.id))
        assertThat(team.owner, `is`(TestConstants.TEAM_RESPONSE.owner))
        assertThat(team.founded, `is`(TestConstants.TEAM_RESPONSE.founded))
        assertThat(team.ended, `is`(TestConstants.TEAM_RESPONSE.ended))
        assertThat(team.followers, `is`(TestConstants.TEAM_RESPONSE.followers))
    }

    @Test
    fun `test invalid getTeamById`() = runBlocking {
        dataSource.service = invalidService

        val result = dataSource.getTeamById("team_id")
        assertThat(result.succeeded, `is`(false))
        result as Result.Error

        assertThat(result.exception.message, `is`(TestConstants.TEAM_ERROR))
    }

    @Test
    fun `test valid followTeam`() = runBlocking {
        dataSource.service = validService
        val result = dataSource.followTeam("team_id")

        assertThat(result.succeeded, `is`(true))
        result as Result.Success

        val team = result.data
        assertThat(team.id, `is`(TestConstants.TEAM_RESPONSE.id))
        assertThat(team.owner, `is`(TestConstants.TEAM_RESPONSE.owner))
        assertThat(team.founded, `is`(TestConstants.TEAM_RESPONSE.founded))
        assertThat(team.ended, `is`(TestConstants.TEAM_RESPONSE.ended))
        assertThat(team.followers, `is`(TestConstants.TEAM_RESPONSE.followers))
    }

    @Test
    fun `test invalid followTeam`() = runBlocking {
        dataSource.service = invalidService
        val result = dataSource.followTeam("team_id")

        assertThat(result.succeeded, `is`(false))
        result as Result.Error

        assertThat(result.exception.message, `is`(TestConstants.TEAM_ERROR))
    }
}