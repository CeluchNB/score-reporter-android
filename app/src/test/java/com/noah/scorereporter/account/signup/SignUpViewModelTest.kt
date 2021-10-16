package com.noah.scorereporter.account.signup

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.noah.scorereporter.TestConstants
import com.noah.scorereporter.account.IUserProfileRepository
import com.noah.scorereporter.fake.FakeUserRepository
import com.noah.scorereporter.getOrAwaitValue
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SignUpViewModelTest {

    private lateinit var viewModel: SignUpViewModel
    private lateinit var repository: IUserProfileRepository

    @Before
    fun initializeViewModel() {
        repository = FakeUserRepository()
        viewModel = SignUpViewModel(repository)
    }

    @Test
    fun `test signup with valid data`() {
        viewModel.signup(
            TestConstants.USER_PROFILE_1.firstName,
            TestConstants.USER_PROFILE_1.lastName,
            TestConstants.USER_PROFILE_1.email,
            "Pass12!"
        )

        assertThat(viewModel.loading.getOrAwaitValue(), `is`(false))

        val result = viewModel.user.getOrAwaitValue().getContentIfNotHandled()

        assertThat(result, `is`(notNullValue()))
        assertThat(result?.firstName, `is`(TestConstants.USER_PROFILE_1.firstName))
        assertThat(result?.lastName, `is`(TestConstants.USER_PROFILE_1.lastName))
        assertThat(result?.email, `is`(TestConstants.USER_PROFILE_1.email))

        val error = viewModel.error.getOrAwaitValue()
        assertThat(error, `is`(nullValue()))
    }

    @Test
    fun `test signup with invalid data`() {
        viewModel.signup(
        "firstName",
        "lastName",
        "invalid@email.com",
        "Pass12!"
        )

        assertThat(viewModel.loading.getOrAwaitValue(), `is`(false))

        val result = viewModel.user.getOrAwaitValue()
        assertThat(result, `is`(nullValue()))

        val error = viewModel.error.getOrAwaitValue().getContentIfNotHandled()
        assertThat(error, `is`(notNullValue()))
    }
}