package com.noah.scorereporter.page

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.noah.scorereporter.MainCoroutineRule
import com.noah.scorereporter.TestConstants
import com.noah.scorereporter.data.network.PageNetworkError
import com.noah.scorereporter.data.network.PageService
import com.noah.scorereporter.data.network.UserNetworkError
import com.noah.scorereporter.fake.FakePageRepository
import com.noah.scorereporter.fake.MockPageClient
import com.noah.scorereporter.pages.PageDataSourceImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
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

        assertThat(result.id, `is`(TestConstants.TEAM_RESPONSE.id))
        assertThat(result.owner, `is`(TestConstants.TEAM_RESPONSE.owner))
        assertThat(result.founded, `is`(TestConstants.TEAM_RESPONSE.founded))
        assertThat(result.ended, `is`(TestConstants.TEAM_RESPONSE.ended))
        assertThat(result.followers, `is`(TestConstants.TEAM_RESPONSE.followers))
    }

    @Test
    fun `test invalid getTeamById`() = runBlocking {
        dataSource.service = invalidService

        try {
            val result = dataSource.getTeamById(TestConstants.TEAM_RESPONSE.id)
        } catch (exception: PageNetworkError) {
            assertThat(exception.message, `is`("Unable to get team"))
        }
    }

    @Test
    fun `test valid followTeam`() = runBlocking {
        dataSource.service = validService
        val result = dataSource.followTeam(TestConstants.TEAM_RESPONSE.id)

        assertThat(result.id, `is`(TestConstants.TEAM_RESPONSE.id))
        assertThat(result.owner, `is`(TestConstants.TEAM_RESPONSE.owner))
        assertThat(result.founded, `is`(TestConstants.TEAM_RESPONSE.founded))
        assertThat(result.ended, `is`(TestConstants.TEAM_RESPONSE.ended))
        assertThat(result.followers, `is`(TestConstants.TEAM_RESPONSE.followers))
    }

    @Test
    fun `test invalid followTeam`() = runBlocking {
        dataSource.service = invalidService
        try {
            val result = dataSource.followTeam(TestConstants.TEAM_RESPONSE.id)
        } catch (exception: PageNetworkError) {
            assertThat(exception.message, `is`("Unable to follow team"))
        }
    }

    @Test
    fun `test valid getSeasonById`() = runBlocking {
        dataSource.service = validService
        val result = dataSource.getSeasonById(TestConstants.SEASON_RESPONSE.id)

        assertThat(result, `is`(TestConstants.SEASON_RESPONSE))
    }

    @Test
    fun `test invalid getSeasonById`() = runBlocking {
        dataSource.service = invalidService
        try {
            val result = dataSource.getSeasonById(TestConstants.SEASON_RESPONSE.id)
        } catch (exception: PageNetworkError) {
            assertThat(exception.message, `is`("Unable to get season"))
        }
    }

    @Test
    fun `test valid getUserById`() = runBlocking {
        dataSource.service = validService
        val result = dataSource.getUserById(TestConstants.USER_PROFILE_1.id)
        assertThat(result, `is`(TestConstants.USER_PROFILE_1))
    }

    @Test
    fun `test invalid getUserById`() = runBlocking {
        dataSource.service = invalidService
        try {
            val result = dataSource.getUserById("badid1")
        } catch (exception: PageNetworkError) {
            assertThat(exception.message, `is`("Unable to get user"))
        }
    }
}