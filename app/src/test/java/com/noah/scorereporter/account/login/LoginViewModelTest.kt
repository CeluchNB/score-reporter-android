package com.noah.scorereporter.account.login

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.noah.scorereporter.Constants
import com.noah.scorereporter.account.UserRepository
import com.noah.scorereporter.getOrAwaitValue
import com.noah.scorereporter.network.Result
import com.noah.scorereporter.network.succeeded
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.collection.IsIn
import org.hamcrest.core.IsNot
import org.hamcrest.core.IsNull
import org.jetbrains.annotations.NotNull
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

        val userProfile = viewModel.userProfile.getOrAwaitValue()
        assertThat(userProfile, `is`(notNullValue()))

        assertThat(userProfile.email, `is`(Constants.USER_PROFILE.email))
        assertThat(userProfile.firstName, `is`(Constants.USER_PROFILE.firstName))
        assertThat(userProfile.lastName, `is`(Constants.USER_PROFILE.lastName))
        assertThat(userProfile.teams.entries, everyItem(IsIn(Constants.USER_PROFILE.teams.entries)))

        val loginError = viewModel.loginError.getOrAwaitValue()
        assertThat(loginError, IsNull())
    }

    @Test
    fun `test login with invalid email`() {
        viewModel.onLoginClicked("invalid@email.com", "password")
        assertThat(viewModel.loading.getOrAwaitValue(), `is`(false))

        val userProfile = viewModel.userProfile.getOrAwaitValue()
        assertThat(userProfile, IsNull())

        val loginError = viewModel.loginError.getOrAwaitValue()
        assertThat(loginError, `is`(Constants.LOGIN_ERROR))
    }
}