package com.noah.scorereporter.page

import androidx.lifecycle.asLiveData
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.noah.scorereporter.MainCoroutineRule
import com.noah.scorereporter.TestConstants
import com.noah.scorereporter.data.local.TeamDao
import com.noah.scorereporter.data.network.PageDataSource
import com.noah.scorereporter.fake.FakePageDataSource
import com.noah.scorereporter.fake.MockPageClient
import com.noah.scorereporter.getOrAwaitValue
import com.noah.scorereporter.pages.IPageRepository
import com.noah.scorereporter.pages.PageRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

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
        `when`(teamDao.getTeamById("id")).thenReturn(flow { emit(TestConstants.TEAM_RESPONSE) })
        `when`(teamDao.hasTeam("id")).thenReturn(true)
        repository = PageRepository(remoteDataSource, teamDao)
    }

    @Test
    fun `test valid getTeamById`() = mainCoroutineRule.runBlockingTest {
        val result = repository.getTeamById("id").asLiveData()
        assertThat(result.getOrAwaitValue(), `is`(TestConstants.TEAM_RESPONSE))
    }
}