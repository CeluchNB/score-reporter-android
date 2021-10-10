package com.noah.scorereporter.account

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.noah.scorereporter.MainCoroutineRule
import com.noah.scorereporter.TestConstants
import com.noah.scorereporter.fake.MockUserClient
import com.noah.scorereporter.data.network.Result
import com.noah.scorereporter.data.network.UserService
import com.noah.scorereporter.data.network.succeeded
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

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

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

        assertThat(result, instanceOf(Result.Success::class.java))
        result as Result.Success
        assertThat(result.data.user.email, `is`(TestConstants.USER_PROFILE.email))
        assertThat(result.data.user.firstName, `is`(TestConstants.USER_PROFILE.firstName))
        assertThat(result.data.user.lastName, `is`(TestConstants.USER_PROFILE.lastName))
        assertThat(result.data.user.teams, `is`(TestConstants.USER_PROFILE.teams))
    }

    @Test
    fun `test invalid login`() = runBlocking {
        dataSource.service = invalidService
        val result = dataSource.login("", "")

        assertThat(result, instanceOf(Result.Error::class.java))
        result as Result.Error
        assertThat(result.exception.message, `is`(TestConstants.LOGIN_ERROR))
    }

    @Test
    fun `test valid getProfile`() = runBlocking {
        dataSource.service = validService
        val result = dataSource.getProfile("jwt")

        assertThat(result, instanceOf(Result.Success::class.java))
        result as Result.Success
        assertThat(result.data.email, `is`(TestConstants.USER_PROFILE.email))
        assertThat(result.data.firstName, `is`(TestConstants.USER_PROFILE.firstName))
        assertThat(result.data.lastName, `is`(TestConstants.USER_PROFILE.lastName))
        assertThat(result.data.teams, `is`(TestConstants.USER_PROFILE.teams))
    }

    @Test
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

        assertThat(result, instanceOf(Result.Success::class.java))
        result as Result.Success
        assertThat(result.data.user.email, `is`(TestConstants.USER_PROFILE.email))
        assertThat(result.data.user.firstName, `is`(TestConstants.USER_PROFILE.firstName))
        assertThat(result.data.user.lastName, `is`(TestConstants.USER_PROFILE.lastName))
        assertThat(result.data.user.teams, `is`(TestConstants.USER_PROFILE.teams))
    }

    @Test
    fun `test invalid signUp`() = runBlocking {
        dataSource.service = invalidService
        val result = dataSource.signUp("", "", "", "")

        assertThat(result, instanceOf(Result.Error::class.java))
        result as Result.Error
        assertThat(result.exception.message, `is`(TestConstants.LOGIN_ERROR))
    }

    @Test
    fun `test successful logout`() = runBlocking {
        dataSource.service = validService
        val result = dataSource.logout("")
        assertThat(result.succeeded, `is`(true))
        result as Result.Success
        assertThat(result.data, `is`(true))
    }

    @Test
    fun `test unsuccessful logout`() = runBlocking {
        dataSource.service = invalidService
        val result = dataSource.logout("")
        assertThat(result.succeeded, `is`(false))
        result as Result.Error
        assertThat(result.exception.message, `is`(""))
    }
}