package com.noah.scorereporter.account

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.noah.scorereporter.MainCoroutineRule
import com.noah.scorereporter.TestConstants
import com.noah.scorereporter.fake.MockUserClient
import com.noah.scorereporter.data.network.Result
import com.noah.scorereporter.data.network.UserNetworkError
import com.noah.scorereporter.data.network.UserService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
@ExperimentalStdlibApi
class UserProfileDataSourceTest {

    private lateinit var dataSource: UserProfileDataSource
    private lateinit var validService: UserService
    private lateinit var invalidService: UserService

//    @get:Rule
//    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun initializeDataSource() {
        dataSource = UserProfileDataSource()
        validService = MockUserClient.createValid()
        invalidService = MockUserClient.createInvalid()
    }

    @Test
    fun `test valid login`() = runBlocking {
        dataSource.service = validService
        val result = dataSource.login("amy@gmail.com", "Pass123!")

        assertThat(result.user.email, `is`(TestConstants.USER_PROFILE_1.email))
        assertThat(result.user.firstName, `is`(TestConstants.USER_PROFILE_1.firstName))
        assertThat(result.user.lastName, `is`(TestConstants.USER_PROFILE_1.lastName))
        assertThat(result.user.teams, `is`(TestConstants.USER_PROFILE_1.teams))
    }

    @Test(expected = UserNetworkError::class)
    fun `test invalid login`() = runBlocking {
        dataSource.service = invalidService
        val result = dataSource.login("", "")
    }

    @Test
    fun `test valid getProfile`() = runBlocking {
        dataSource.service = validService
        val result = dataSource.getProfile("jwt")

        assertThat(result.email, `is`(TestConstants.USER_PROFILE_1.email))
        assertThat(result.firstName, `is`(TestConstants.USER_PROFILE_1.firstName))
        assertThat(result.lastName, `is`(TestConstants.USER_PROFILE_1.lastName))
        assertThat(result.teams, `is`(TestConstants.USER_PROFILE_1.teams))
    }

    @Test(expected = UserNetworkError::class)
    fun `test invalid getProfile`() = runBlocking {
        dataSource.service = invalidService
        val result = dataSource.getProfile("jwt")

        assertThat(result, instanceOf(Result.Error::class.java))
        result as Result.Error
        assertThat(result.exception.message, `is`(TestConstants.LOGIN_ERROR))
    }

    @Test
    fun `test valid signUp`() = runBlocking {
        dataSource.service = validService
        val result = dataSource.signUp("", "", "", "")

        assertThat(result.user.email, `is`(TestConstants.USER_PROFILE_1.email))
        assertThat(result.user.firstName, `is`(TestConstants.USER_PROFILE_1.firstName))
        assertThat(result.user.lastName, `is`(TestConstants.USER_PROFILE_1.lastName))
        assertThat(result.user.teams, `is`(TestConstants.USER_PROFILE_1.teams))
    }

    @Test(expected = UserNetworkError::class)
    fun `test invalid signUp`() = runBlocking {
        dataSource.service = invalidService
        val result = dataSource.signUp("", "", "", "")
    }

    @Test
    fun `test successful logout`() = runBlocking {
        dataSource.service = validService
        dataSource.logout("")
    }

    @Test(expected = UserNetworkError::class)
    fun `test unsuccessful logout`() = runBlocking {
        dataSource.service = invalidService
        dataSource.logout("")
    }
}