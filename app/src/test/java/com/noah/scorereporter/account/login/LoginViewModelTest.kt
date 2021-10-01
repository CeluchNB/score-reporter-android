package com.noah.scorereporter.account.login

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.noah.scorereporter.TestConstants
import com.noah.scorereporter.account.IUserProfileRepository
import com.noah.scorereporter.fake.FakeUserRepository
import com.noah.scorereporter.getOrAwaitValue
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.collection.IsIn
import org.hamcrest.core.IsNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginViewModelTest {

    private lateinit var repository: IUserProfileRepository
    private lateinit var viewModel: LoginViewModel

    @Before
    fun initializeViewModel() {
        repository = FakeUserRepository()
        viewModel = LoginViewModel(repository)
    }

    @Test
    fun `login with valid email`() {
        viewModel.onLoginClicked("email@email.com", "password")
        assertThat(viewModel.loading.getOrAwaitValue(), `is`(false))

        val userProfile = viewModel.userProfile.getOrAwaitValue()
        assertThat(userProfile, `is`(notNullValue()))

        assertThat(userProfile?.email, `is`(TestConstants.USER_PROFILE.email))
        assertThat(userProfile?.firstName, `is`(TestConstants.USER_PROFILE.firstName))
        assertThat(userProfile?.lastName, `is`(TestConstants.USER_PROFILE.lastName))
        assertThat(userProfile?.teams?.entries, everyItem(IsIn(TestConstants.USER_PROFILE.teams.entries)))

        val loginError = viewModel.loginError.getOrAwaitValue()
        assertThat(loginError, IsNull())
    }

    @Test
    fun `login with invalid email`() {
        viewModel.onLoginClicked("invalid@email.com", "password")
        assertThat(viewModel.loading.getOrAwaitValue(), `is`(false))

        val userProfile = viewModel.userProfile.getOrAwaitValue()
        assertThat(userProfile, IsNull())

        val loginError = viewModel.loginError.getOrAwaitValue()
        assertThat(loginError, `is`(TestConstants.LOGIN_ERROR))
    }
}