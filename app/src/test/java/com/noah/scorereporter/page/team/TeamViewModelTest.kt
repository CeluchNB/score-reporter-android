package com.noah.scorereporter.page.team

import com.noah.scorereporter.TestConstants
import com.noah.scorereporter.fake.FakePageRepository
import com.noah.scorereporter.getOrAwaitValue
import com.noah.scorereporter.pages.IPageRepository
import com.noah.scorereporter.pages.team.TeamViewModel
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import java.util.concurrent.TimeoutException

class TeamViewModelTest {

    private lateinit var repository: IPageRepository
    private lateinit var viewModel: TeamViewModel

    @Before
    fun setUp() {
        repository = FakePageRepository()
        viewModel = TeamViewModel(repository)
    }

    @Test
    fun `test valid fetchTeam`() {
        (repository as FakePageRepository).valid = true
        viewModel.fetchTeam(TestConstants.TEAM_RESPONSE.id)
        assertThat(viewModel.loading.getOrAwaitValue(), `is`(false))
        assertThat(viewModel.team.getOrAwaitValue(), `is`(TestConstants.TEAM_RESPONSE))
    }

    @Test
    fun `test invalid fetchTeam`() {
        (repository as FakePageRepository).valid = false
        viewModel.fetchTeam(TestConstants.TEAM_RESPONSE.id)
        assertThat(viewModel.loading.getOrAwaitValue(), `is`(false))
        try {
            viewModel.team.getOrAwaitValue()
        } catch (exception: TimeoutException) {
            assertThat(exception.message, `is`("LiveData value was never set."))
        }
    }

    @Test
    fun `test valid follow`() {
        (repository as FakePageRepository).valid = true
        viewModel.follow()
        assertThat(viewModel.loading.getOrAwaitValue(), `is`(false))
        assertThat(viewModel.team.getOrAwaitValue(), `is`(TestConstants.TEAM_RESPONSE))
    }

    @Test
    fun `test invalid follow`() {
        (repository as FakePageRepository).valid = false
        viewModel.follow()
        assertThat(viewModel.loading.getOrAwaitValue(), `is`(false))
        try {
            viewModel.team.getOrAwaitValue()
        } catch (exception: TimeoutException) {
            assertThat(exception.message, `is`("LiveData value was never set."))
        }
    }
}