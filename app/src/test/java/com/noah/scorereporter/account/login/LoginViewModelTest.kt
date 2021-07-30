package com.noah.scorereporter.account.login

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.noah.scorereporter.Constants
import com.noah.scorereporter.account.UserRepository
import com.noah.scorereporter.getOrAwaitValue
import com.noah.scorereporter.network.Result
import com.noah.scorereporter.network.succeeded
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.everyItem
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.collection.IsIn
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginViewModelUnitTest {

    private lateinit var repository: UserRepository
    private lateinit var viewModel: LoginViewModel

    @Before
    fun initializeViewModel() {
        repository = FakeUserRepository()
        viewModel = LoginViewModel(repository)
    }

    @Test
    fun `test login with valid email`() {
        viewModel.onLoginClicked("email@email.com", "password")
        assertThat(viewModel.loading.getOrAwaitValue(), `is`(false))

        val result = viewModel.userProfile.getOrAwaitValue()
        assertThat(result.succeeded, `is`(true))

        result as Result.Success
        assertThat(result.data.email, `is`(Constants.USER_PROFILE.email))
        assertThat(result.data.firstName, `is`(Constants.USER_PROFILE.firstName))
        assertThat(result.data.lastName, `is`(Constants.USER_PROFILE.lastName))
        assertThat(result.data.teams.entries, everyItem(IsIn(Constants.USER_PROFILE.teams.entries)))
    }

    @Test
    fun `test login with invalid email`() {
        viewModel.onLoginClicked("invalid@email.com", "password")
        assertThat(viewModel.loading.getOrAwaitValue(), `is`(false))

        val result = viewModel.userProfile.getOrAwaitValue()
        assertThat(result.succeeded, `is`(false))

        result as Result.Error
        assertThat(result.exception.message, `is`("Invalid email or password"))
    }
}