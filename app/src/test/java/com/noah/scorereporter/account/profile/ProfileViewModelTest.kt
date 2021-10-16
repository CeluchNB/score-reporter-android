package com.noah.scorereporter.account.profile

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.noah.scorereporter.TestConstants
import com.noah.scorereporter.account.IUserProfileRepository
import com.noah.scorereporter.fake.FakeUserRepository
import com.noah.scorereporter.getOrAwaitValue
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.collection.IsIn
import org.hamcrest.core.IsNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProfileViewModelTest {

    private lateinit var viewModel: ProfileViewModel
    private lateinit var repository: IUserProfileRepository

    @Before
    fun initializeViewModel() {
        repository = FakeUserRepository()
        viewModel = ProfileViewModel(repository)
    }

    @Test
    fun `test getUserProfile with existing token`() {
        viewModel.fetchUserProfile()
        assertThat(viewModel.loading.getOrAwaitValue(), `is`(false))

        val userProfile = viewModel.userProfile.getOrAwaitValue()
        assertThat(userProfile, `is`(CoreMatchers.notNullValue()))

        assertThat(userProfile?.email, `is`(TestConstants.USER_PROFILE_1.email))
        assertThat(userProfile?.firstName, `is`(TestConstants.USER_PROFILE_1.firstName))
        assertThat(userProfile?.lastName, `is`(TestConstants.USER_PROFILE_1.lastName))
        assertThat(userProfile?.teams, `is`(TestConstants.USER_PROFILE_1.teams)
        )

        assertThat(viewModel.getProfileError.getOrAwaitValue(), `is`(false))
    }

    @Test
    fun `test getUserProfile without existing token`() {
        (repository as FakeUserRepository).valid = false
        viewModel.fetchUserProfile()

        assertThat(viewModel.loading.getOrAwaitValue(), `is`(false))

        val userProfile = viewModel.userProfile.getOrAwaitValue()
        assertThat(userProfile, IsNull())
        assertThat(viewModel.getProfileError.getOrAwaitValue(), `is`(true))
    }

    @Test
    fun `test hasSavedToken`() {
        (repository as FakeUserRepository).valid = true
        assertThat(viewModel.hasSavedToken(), `is`(true))

        (repository as FakeUserRepository).valid = false
        assertThat(viewModel.hasSavedToken(), `is`(false))
    }

    @Test
    fun `test valid logout`() {
        (repository as FakeUserRepository).valid = true
        viewModel.logout()
        assertThat(viewModel.logoutLoading.getOrAwaitValue(), `is`(false))
        val event = viewModel.logoutSuccess.getOrAwaitValue()
        assertThat(event.getContentIfNotHandled(), `is`(true))
    }


    @Test
    fun `test invalid logout`() {
        (repository as FakeUserRepository).valid = false
        viewModel.logout()
        assertThat(viewModel.logoutLoading.getOrAwaitValue(), `is`(false))
        val event = viewModel.logoutSuccess.getOrAwaitValue()
        assertThat(event.getContentIfNotHandled(), `is`(false))
    }

    @Test
    fun `test setUser`() {
        assertThat(viewModel.userProfile.getOrAwaitValue(), IsNull())
        viewModel.setUserProfile(TestConstants.USER_PROFILE_1)
        assertThat(viewModel.userProfile.getOrAwaitValue(), `is`(TestConstants.USER_PROFILE_1))
    }
}