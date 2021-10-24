package com.noah.scorereporter.pages.season

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.noah.scorereporter.MainCoroutineRule
import com.noah.scorereporter.TestConstants
import com.noah.scorereporter.data.network.DispatcherProvider
import com.noah.scorereporter.fake.FakePageRepository
import com.noah.scorereporter.getOrAwaitValue
import com.noah.scorereporter.pages.IPageRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class SeasonViewModelTest {

    private lateinit var viewModel: SeasonViewModel
    private lateinit var repository: IPageRepository
    private lateinit var dispatcherProvider: DispatcherProvider

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        repository = FakePageRepository()
        dispatcherProvider = mainCoroutineRule.testDispatchers
        viewModel = SeasonViewModel(repository, dispatcherProvider)
    }

    @Test
    fun `test season change with valid repository`() = mainCoroutineRule.runBlockingTest {
        (repository as FakePageRepository).valid = true
        viewModel.id.value = TestConstants.SEASON_RESPONSE.id

        val result = viewModel.season.getOrAwaitValue()
        assertThat(result, `is`(TestConstants.SEASON_RESPONSE))
    }

    @Test
    fun `test season change with invalid repository`() = mainCoroutineRule.runBlockingTest {
        (repository as FakePageRepository).valid = false
        viewModel.id.value = TestConstants.SEASON_RESPONSE.id

        try {
            val result = viewModel.season.getOrAwaitValue()
        } catch (exception: TimeoutException) {
            assertThat(exception.message, `is`(TestConstants.LIVE_DATA_ERROR))
        }
    }

    @Test
    fun `test game change with valid repository`() = mainCoroutineRule.runBlockingTest {
        (repository as FakePageRepository).valid = true
        viewModel.id.value = TestConstants.SEASON_RESPONSE.id

        val result = viewModel.games.getOrAwaitValue()
        assertThat(result, `is`(listOf(TestConstants.GAME_1, TestConstants.GAME_2)))
    }

    @Test
    fun `test game change with invalid repository`() = mainCoroutineRule.runBlockingTest {
        (repository as FakePageRepository).valid = false
        viewModel.id.value = TestConstants.SEASON_RESPONSE.id

        try {
            val result = viewModel.games.getOrAwaitValue()
        } catch (exception: TimeoutException) {
            assertThat(exception.message, `is`(TestConstants.LIVE_DATA_ERROR))
        }
    }
}