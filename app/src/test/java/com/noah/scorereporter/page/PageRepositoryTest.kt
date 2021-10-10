package com.noah.scorereporter.page

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.noah.scorereporter.MainCoroutineRule
import com.noah.scorereporter.TestConstants
import com.noah.scorereporter.data.local.TeamDao
import com.noah.scorereporter.data.network.PageDataSource
import com.noah.scorereporter.data.network.Result
import com.noah.scorereporter.data.network.succeeded
import com.noah.scorereporter.fake.FakePageDataSource
import com.noah.scorereporter.pages.IPageRepository
import com.noah.scorereporter.pages.PageRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class PageRepositoryTest {

    private lateinit var repository: IPageRepository
    private lateinit var remoteDataSource: PageDataSource
    private lateinit var teamDao: TeamDao

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        remoteDataSource = FakePageDataSource()
        teamDao = mock(TeamDao::class.java)
        `when`(teamDao.getTeamById(TestConstants.TEAM_RESPONSE.id)).thenReturn(TestConstants.TEAM_RESPONSE)
        `when`(teamDao.getTeamById("id1")).thenReturn(null)
        `when`(teamDao.hasTeam(TestConstants.TEAM_RESPONSE.id)).thenReturn(true)
        repository = PageRepository(remoteDataSource, teamDao)
    }

    @Test
    fun `test getTeamById with existing user`() = mainCoroutineRule.runBlockingTest {
        (remoteDataSource as FakePageDataSource).valid = true
        val result = repository.getTeamById(TestConstants.TEAM_RESPONSE.id)
        verify(teamDao).hasTeam(TestConstants.TEAM_RESPONSE.id)
        verify(teamDao, times(0)).save(listOf(TestConstants.TEAM_RESPONSE))
        verify(teamDao).getTeamById(TestConstants.TEAM_RESPONSE.id)
        assertThat(result.succeeded, `is`(true))
        result as Result.Success
        assertThat(result.data, `is`(TestConstants.TEAM_RESPONSE))
    }

    @Test
    fun `test getTeamById with user not found`() = mainCoroutineRule.runBlockingTest {
        (remoteDataSource as FakePageDataSource).valid = false
        val result = repository.getTeamById("id1")
        verify(teamDao).hasTeam("id1")
        verify(teamDao, times(0)).save(listOf(TestConstants.TEAM_RESPONSE))
        verify(teamDao).getTeamById("id1")
        assertThat(result.succeeded, `is`(false))
        result as Result.Error
        assertThat(result.exception.message, `is`("No team found"))
    }

    @Test
    fun `test getTeamById after adding user`() = mainCoroutineRule.runBlockingTest {
        (remoteDataSource as FakePageDataSource).valid = true
        `when`(teamDao.getTeamById("id2")).thenReturn(TestConstants.TEAM_RESPONSE)

        val result = repository.getTeamById("id2")
        verify(teamDao).hasTeam("id2")
        verify(teamDao).save(listOf(TestConstants.TEAM_RESPONSE))
        verify(teamDao).getTeamById("id2")

        assertThat(result.succeeded, `is`(true))
        result as Result.Success
        assertThat(result.data, `is`(TestConstants.TEAM_RESPONSE))
    }

    @Test
    fun `test followTeam with existing user`() = mainCoroutineRule.runBlockingTest {
        (remoteDataSource as FakePageDataSource).valid = true

        val result = repository.followTeam(TestConstants.TEAM_RESPONSE.id)
        verify(teamDao).save(listOf(TestConstants.TEAM_RESPONSE))
        verify(teamDao).getTeamById(TestConstants.TEAM_RESPONSE.id)

        assertThat(result.succeeded, `is`(true))
        result as Result.Success
        assertThat(result.data, `is`(TestConstants.TEAM_RESPONSE))
    }

    @Test
    fun `test followTeam with non existent user`() = mainCoroutineRule.runBlockingTest {
        (remoteDataSource as FakePageDataSource).valid = false

        val result = repository.followTeam("id1")
        verify(teamDao, times(0)).save(listOf(TestConstants.TEAM_RESPONSE))
        verify(teamDao).getTeamById("id1")

        assertThat(result.succeeded, `is`(false))
        result as Result.Error
        assertThat(result.exception.message, `is`("No team found"))
    }
}