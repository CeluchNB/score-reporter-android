package com.noah.scorereporter.account.profile

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.noah.scorereporter.TestConstants
import com.noah.scorereporter.account.IUserProfileRepository
import com.noah.scorereporter.account.UserProfileRepository
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
        viewModel.getUserProfile()
        assertThat(viewModel.loading.getOrAwaitValue(), `is`(false))

        val userProfile = viewModel.user.getOrAwaitValue()
        assertThat(userProfile, `is`(CoreMatchers.notNullValue()))

        assertThat(userProfile?.email, `is`(TestConstants.USER_PROFILE.email))
        assertThat(userProfile?.firstName, `is`(TestConstants.USER_PROFILE.firstName))
        assertThat(userProfile?.lastName, `is`(TestConstants.USER_PROFILE.lastName))
        assertThat(userProfile?.teams?.entries,
            CoreMatchers.everyItem(IsIn(TestConstants.USER_PROFILE.teams.entries))
        )

        assertThat(viewModel.getProfileError.getOrAwaitValue(), `is`(false))
    }

    @Test
    fun `test getUserProfile without existing token`() {
        (repository as FakeUserRepository).validToken = false
        viewModel.getUserProfile()

        assertThat(viewModel.loading.getOrAwaitValue(), `is`(false))

        val userProfile = viewModel.user.getOrAwaitValue()
        assertThat(userProfile, IsNull())
        assertThat(viewModel.getProfileError.getOrAwaitValue(), `is`(true))
    }


}