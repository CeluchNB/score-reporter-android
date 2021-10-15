package com.noah.scorereporter.page

import androidx.lifecycle.asLiveData
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.noah.scorereporter.MainCoroutineRule
import com.noah.scorereporter.TestConstants
import com.noah.scorereporter.data.local.TeamDao
import com.noah.scorereporter.data.network.PageDataSource
import com.noah.scorereporter.fake.FakePageDataSource
import com.noah.scorereporter.getOrAwaitValue
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
import java.util.concurrent.TimeoutException

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
        `when`(teamDao.getTeamById(TestConstants.TEAM_RESPONSE.id)).thenReturn(
            flow { emit(TestConstants.TEAM_RESPONSE) }
        )
        `when`(teamDao.getTeamById("id1")).thenReturn(flow { })
        `when`(teamDao.hasTeam(TestConstants.TEAM_RESPONSE.id)).thenReturn(true)
        repository = PageRepository(remoteDataSource, teamDao)
    }

    @Test
    fun `test getTeamById with existing user`() = mainCoroutineRule.runBlockingTest {
        (remoteDataSource as FakePageDataSource).valid = true
        val result = repository.getTeamById(TestConstants.TEAM_RESPONSE.id).asLiveData()
        verify(teamDao).hasTeam(TestConstants.TEAM_RESPONSE.id)
        verify(teamDao, times(0)).save(listOf(TestConstants.TEAM_RESPONSE))
        verify(teamDao).getTeamById(TestConstants.TEAM_RESPONSE.id)
        assertThat(result.getOrAwaitValue(), `is`(TestConstants.TEAM_RESPONSE))
    }

    @Test
    fun `test getTeamById with user not found`() = mainCoroutineRule.runBlockingTest {
        (remoteDataSource as FakePageDataSource).valid = false
        val result = repository.getTeamById("id1").asLiveData()
        verify(teamDao).hasTeam("id1")
        verify(teamDao, times(0)).save(listOf(TestConstants.TEAM_RESPONSE))
        verify(teamDao).getTeamById("id1")

        try {
            result.getOrAwaitValue()
        } catch (exception: TimeoutException) {
            assertThat(exception.message, `is`("LiveData value was never set."))
        }
    }

    @Test
    fun `test getTeamById after adding user`() = mainCoroutineRule.runBlockingTest {
        (remoteDataSource as FakePageDataSource).valid = true
        `when`(teamDao.getTeamById("id2")).thenReturn(flow { emit(TestConstants.TEAM_RESPONSE) })

        val result = repository.getTeamById("id2").asLiveData()
        verify(teamDao).hasTeam("id2")
        verify(teamDao).save(listOf(TestConstants.TEAM_RESPONSE))
        verify(teamDao).getTeamById("id2")

        assertThat(result.getOrAwaitValue(), `is`(TestConstants.TEAM_RESPONSE))
    }

    @Test
    fun `test followTeam with existing user`() = mainCoroutineRule.runBlockingTest {
        (remoteDataSource as FakePageDataSource).valid = true

        val result = repository.followTeam(TestConstants.TEAM_RESPONSE.id).asLiveData()
        verify(teamDao).save(listOf(TestConstants.TEAM_RESPONSE))
        verify(teamDao).getTeamById(TestConstants.TEAM_RESPONSE.id)

        assertThat(result.getOrAwaitValue(), `is`(TestConstants.TEAM_RESPONSE))
    }

    @Test
    fun `test followTeam with non existent user`() = mainCoroutineRule.runBlockingTest {
        (remoteDataSource as FakePageDataSource).valid = false

        val result = repository.followTeam("id1").asLiveData()
        verify(teamDao, times(0)).save(listOf(TestConstants.TEAM_RESPONSE))
        verify(teamDao).getTeamById("id1")

        try {
            result.getOrAwaitValue()
        } catch (exception: TimeoutException) {
            assertThat(exception.message, `is`("LiveData value was never set."))
        }
    }

    @Test
    fun `test getSeasonsOfTeam with valid source`() = mainCoroutineRule.runBlockingTest {
        (remoteDataSource as FakePageDataSource).valid = true

        val result = repository.getSeasonsOfTeam(
            listOf(TestConstants.SEASON_RESPONSE.id, TestConstants.SEASON_RESPONSE_2.id)
        ).asLiveData()

        val list = result.getOrAwaitValue()
        assertThat(list.size, `is`(2))
        assertThat(list[0], `is`(TestConstants.SEASON_RESPONSE))
        assertThat(list[1], `is`(TestConstants.SEASON_RESPONSE_2))
    }

    @Test
    fun `test getSeasonsOfTeam with bad list`() = mainCoroutineRule.runBlockingTest {
        (remoteDataSource as FakePageDataSource).valid = true

        val result = repository.getSeasonsOfTeam(
            listOf("bad_id_1", "bad_id_2")
        ).asLiveData()

        try {
            result.getOrAwaitValue()
        } catch (exception: TimeoutException) {
            assertThat(exception.message, `is`("LiveData value was never set."))
        }
    }

    @Test
    fun `test getSeasonsOfTeam with invalid source`() = mainCoroutineRule.runBlockingTest {
        (remoteDataSource as FakePageDataSource).valid = false

        val result = repository.getSeasonsOfTeam(
            listOf(TestConstants.SEASON_RESPONSE.id, TestConstants.SEASON_RESPONSE_2.id)
        ).asLiveData()

        try {
            result.getOrAwaitValue()
        } catch (exception: TimeoutException) {
            assertThat(exception.message, `is`("LiveData value was never set."))
        }
    }
}