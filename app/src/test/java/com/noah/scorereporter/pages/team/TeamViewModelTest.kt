package com.noah.scorereporter.pages.team

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.noah.scorereporter.MainCoroutineRule
import com.noah.scorereporter.TestConstants
import com.noah.scorereporter.fake.FakePageRepository
import com.noah.scorereporter.getOrAwaitValue
import com.noah.scorereporter.pages.IPageRepository
import com.noah.scorereporter.pages.team.TeamViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeoutException

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class TeamViewModelTest {

    private lateinit var repository: IPageRepository
    private lateinit var viewModel: TeamViewModel

    @get:Rule
    val instantTaskRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        repository = FakePageRepository()
        viewModel = TeamViewModel(repository, mainCoroutineRule.testDispatchers)
    }

    @Test
    fun `test valid fetch team`() = mainCoroutineRule.dispatcher.runBlockingTest {
        // flakey
        (repository as FakePageRepository).valid = true
        viewModel.id.value = TestConstants.TEAM_RESPONSE.id
        assertThat(viewModel.loading.getOrAwaitValue(), `is`(false))
        assertThat(viewModel.team.getOrAwaitValue(), `is`(TestConstants.TEAM_RESPONSE))
    }

    @Test
    fun `test invalid fetch team`() = mainCoroutineRule.dispatcher.runBlockingTest {
        (repository as FakePageRepository).valid = false
        viewModel.id.value = TestConstants.TEAM_RESPONSE.id
        assertThat(viewModel.loading.getOrAwaitValue(), `is`(false))
        try {
            viewModel.team.getOrAwaitValue()
        } catch (exception: TimeoutException) {
            assertThat(exception.message, `is`(TestConstants.LIVE_DATA_ERROR))
        }
    }

    @Test
    fun `test valid get multiple seasons`() = mainCoroutineRule.dispatcher.runBlockingTest {
        (repository as FakePageRepository).valid = true
        viewModel.id.value = TestConstants.TEAM_RESPONSE.id
        assertThat(viewModel.team.getOrAwaitValue(), `is`(TestConstants.TEAM_RESPONSE))

        val seasons = viewModel.seasons.getOrAwaitValue()
        assertThat(seasons.size, `is`(2))
        assertThat(seasons[0], `is`(TestConstants.SEASON_RESPONSE))
        assertThat(seasons[1], `is`(TestConstants.SEASON_RESPONSE_2))
    }

    @Test
    fun `test invalid get multiple seasons`() = mainCoroutineRule.dispatcher.runBlockingTest {
        (repository as FakePageRepository).valid = false
        viewModel.id.value = TestConstants.TEAM_RESPONSE.id

        try {
            viewModel.team.getOrAwaitValue()
        } catch (exception: TimeoutException) {
            assertThat(exception.message, `is`(TestConstants.LIVE_DATA_ERROR))
        }

        try {
            viewModel.seasons.getOrAwaitValue()
        } catch (exception: TimeoutException) {
            assertThat(exception.message, `is`(TestConstants.LIVE_DATA_ERROR))
        }
    }

    @Test
    fun `test valid get multiple followers`() = mainCoroutineRule.dispatcher.runBlockingTest {
        // flakey
        (repository as FakePageRepository).valid = true
        viewModel.id.value = TestConstants.TEAM_RESPONSE.id
        assertThat(viewModel.team.getOrAwaitValue(), `is`(TestConstants.TEAM_RESPONSE))

        val followers = viewModel.followers.getOrAwaitValue()
        assertThat(followers.size, `is`(2))
        assertThat(followers[0], `is`(TestConstants.FOLLOWER_1))
        assertThat(followers[1], `is`(TestConstants.FOLLOWER_2))
    }

    @Test
    fun `test invalid get multiple followers`() = mainCoroutineRule.dispatcher.runBlockingTest {
        (repository as FakePageRepository).valid = false
        viewModel.id.value = TestConstants.TEAM_RESPONSE.id

        try {
            viewModel.team.getOrAwaitValue()
        } catch (exception: TimeoutException) {
            assertThat(exception.message, `is`(TestConstants.LIVE_DATA_ERROR))
        }

        try {
            viewModel.followers.getOrAwaitValue()
        } catch (exception: TimeoutException) {
            assertThat(exception.message, `is`(TestConstants.LIVE_DATA_ERROR))
        }
    }

    @Test
    fun `test valid follow`() = mainCoroutineRule.dispatcher.runBlockingTest {
        (repository as FakePageRepository).valid = true
        viewModel.id.value = TestConstants.TEAM_RESPONSE.id
        viewModel.follow()
        assertThat(viewModel.loading.getOrAwaitValue(), `is`(false))
        assertThat(viewModel.followSuccess.getOrAwaitValue(), `is`(true))
        assertThat(viewModel.team.getOrAwaitValue(), `is`(TestConstants.TEAM_RESPONSE))
    }

    @Test
    fun `test invalid follow`() {
        (repository as FakePageRepository).valid = false
        viewModel.id.value = TestConstants.TEAM_RESPONSE.id
        viewModel.follow()
        assertThat(viewModel.loading.getOrAwaitValue(), `is`(false))
        assertThat(viewModel.followSuccess.getOrAwaitValue(), `is`(false))
        try {
            viewModel.team.getOrAwaitValue()
        } catch (exception: TimeoutException) {
            assertThat(exception.message, `is`(TestConstants.LIVE_DATA_ERROR))
        }
    }

    @Test
    fun `test valid follow with no id`() {
        (repository as FakePageRepository).valid = true
        viewModel.follow()
        assertThat(viewModel.loading.getOrAwaitValue(), `is`(false))
        assertThat(viewModel.followSuccess.getOrAwaitValue(), `is`(false))
        try {
            viewModel.team.getOrAwaitValue()
        } catch (exception: TimeoutException) {
            assertThat(exception.message, `is`(TestConstants.LIVE_DATA_ERROR))
        }
    }
}