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
        viewModel.login("email@email.com", "password")
        assertThat(viewModel.loading.getOrAwaitValue(), `is`(false))

        val userProfile = viewModel.userProfile.getOrAwaitValue()
        assertThat(userProfile, `is`(notNullValue()))

        assertThat(userProfile?.email, `is`(TestConstants.USER_PROFILE_1.email))
        assertThat(userProfile?.firstName, `is`(TestConstants.USER_PROFILE_1.firstName))
        assertThat(userProfile?.lastName, `is`(TestConstants.USER_PROFILE_1.lastName))
        assertThat(userProfile?.teams, `is`(TestConstants.USER_PROFILE_1.teams))

        val loginError = viewModel.loginError.getOrAwaitValue()
        assertThat(loginError, IsNull())
    }

    @Test
    fun `login with invalid email`() {
        viewModel.login("invalid@email.com", "password")
        assertThat(viewModel.loading.getOrAwaitValue(), `is`(false))

        val userProfile = viewModel.userProfile.getOrAwaitValue()
        assertThat(userProfile, IsNull())

        val loginError = viewModel.loginError.getOrAwaitValue()
        assertThat(loginError, `is`(TestConstants.LOGIN_ERROR))
    }
}