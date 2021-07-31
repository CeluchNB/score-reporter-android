package com.noah.scorereporter.account

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.noah.scorereporter.Constants
import com.noah.scorereporter.MainCoroutineRule
import com.noah.scorereporter.fake.FakeUserDataSource
import com.noah.scorereporter.network.Result
import com.noah.scorereporter.network.UserDataSource
import com.noah.scorereporter.network.succeeded
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.collection.IsIn
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class UserProfileRepositoryTest {

    private lateinit var repository: UserRepository
    private lateinit var dataSource: UserDataSource

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun initializeRepository() {
        dataSource = FakeUserDataSource()
        (dataSource as FakeUserDataSource).shouldReturnError = false
        repository = UserProfileRepository(dataSource)
    }

    @Test
    fun `login with valid email`() = mainCoroutineRule.runBlockingTest {
        val result = repository.login("email@email.com", "password")
        assertThat(result.succeeded, `is`(true))

        result as Result.Success
        assertThat(result.data.email, `is`(Constants.USER_RESPONSE.email))
        assertThat(result.data.firstName, `is`(Constants.USER_RESPONSE.firstName))
        assertThat(result.data.lastName, `is`(Constants.USER_RESPONSE.lastName))
        assertThat(result.data.teams.entries,
            CoreMatchers.everyItem(IsIn(Constants.USER_RESPONSE.teams.entries))
        )

        TODO("assert jwt is saved in AccountManager")
    }

    @Test
    fun `login with invalid email`() = mainCoroutineRule.runBlockingTest {
        val result = repository.login("invalid@email.com", "password")
        assertThat(result.succeeded, `is`(false))

        result as Result.Error
        assertThat(result.exception.message, `is`(Constants.LOGIN_ERROR))
        TODO("assert jwt is NOT saved in AccountManager")
    }

    @Test
    fun `getProfile with valid jwt`() = mainCoroutineRule.runBlockingTest {
        val result = repository.getProfile()

        assertThat(result.succeeded, `is`(true))

        result as Result.Success
        assertThat(result.data.email, `is`(Constants.USER_RESPONSE.email))
        assertThat(result.data.firstName, `is`(Constants.USER_RESPONSE.firstName))
        assertThat(result.data.lastName, `is`(Constants.USER_RESPONSE.lastName))
        assertThat(result.data.teams.entries,
            CoreMatchers.everyItem(IsIn(Constants.USER_RESPONSE.teams.entries))
        )
    }

    @Test
    fun `getProfile with invalid jwt`() = mainCoroutineRule.runBlockingTest {
        (dataSource as FakeUserDataSource).shouldReturnError = true
        val result = repository.getProfile()
        assertThat(result.succeeded, `is`(false))

        result as Result.Error
        assertThat(result.exception.message, `is`(Constants.LOGIN_ERROR))
    }
}