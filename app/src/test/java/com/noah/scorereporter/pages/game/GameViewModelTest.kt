package com.noah.scorereporter.pages.game

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
class GameViewModelTest {

    private lateinit var viewModel: GameViewModel
    private lateinit var repository: IPageRepository
    private lateinit var dispatchers: DispatcherProvider

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        repository = FakePageRepository()
        dispatchers = mainCoroutineRule.testDispatchers
        viewModel = GameViewModel(repository, dispatchers)
    }

    @Test
    fun `test id change with valid repository`() = mainCoroutineRule.runBlockingTest {
        (repository as FakePageRepository).valid = true
        viewModel.id.value = TestConstants.GAME_1.id

        assertThat(viewModel.game.getOrAwaitValue(), `is`(TestConstants.GAME_ITEM_1))
    }

    @Test
    fun `test id change with invalid repository`() = mainCoroutineRule.runBlockingTest {
        (repository as FakePageRepository).valid = false
        viewModel.id.value = TestConstants.GAME_1.id

        try {
            val result = viewModel.game.getOrAwaitValue()
        } catch (exception: TimeoutException) {
            assertThat(exception.message, `is`(TestConstants.LIVE_DATA_ERROR))
        }
    }
}