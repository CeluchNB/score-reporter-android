package com.noah.scorereporter.account

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.noah.scorereporter.TestConstants
import com.noah.scorereporter.MainCoroutineRule
import com.noah.scorereporter.fake.FakeUserDataSource
import com.noah.scorereporter.network.Result
import com.noah.scorereporter.network.UserDataSource
import com.noah.scorereporter.network.succeeded
import com.noah.scorereporter.util.Constants
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.collection.IsIn
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class UserProfileRepositoryTest {

    private lateinit var repository: IUserProfileRepository
    private lateinit var dataSource: UserDataSource
    private lateinit var context: Context
    private lateinit var masterKey: MasterKey
    private lateinit var sharedPrefs: SharedPreferences

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun initializeRepository() {
        context = ApplicationProvider.getApplicationContext()
        dataSource = FakeUserDataSource()
        (dataSource as FakeUserDataSource).shouldReturnError = false

        masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        sharedPrefs = EncryptedSharedPreferences.create(
            context,
            Constants.SHARED_PREFS,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        repository = UserProfileRepository(dataSource, sharedPrefs)
    }

    @Test
    fun `login with valid email`() = mainCoroutineRule.runBlockingTest {
        val result = repository.login("email@email.com", "password")
        assertThat(result.succeeded, `is`(true))

        result as Result.Success
        assertThat(result.data.email, `is`(TestConstants.USER_RESPONSE.user.email))
        assertThat(result.data.firstName, `is`(TestConstants.USER_RESPONSE.user.firstName))
        assertThat(result.data.lastName, `is`(TestConstants.USER_RESPONSE.user.lastName))
        assertThat(result.data.teams.entries,
            CoreMatchers.everyItem(IsIn(TestConstants.USER_RESPONSE.user.teams.entries))
        )

        val jwt = sharedPrefs.getString(Constants.USER_TOKEN, "bad_token")
        assertThat(jwt, `is`(not("bad_token")))
        assertThat(jwt, `is`(TestConstants.USER_RESPONSE.token))
    }

    @Test
    fun `login with invalid email`() = mainCoroutineRule.runBlockingTest {
        val result = repository.login("invalid@email.com", "password")
        assertThat(result.succeeded, `is`(false))

        result as Result.Error
        assertThat(result.exception.message, `is`(TestConstants.LOGIN_ERROR))

        val jwt = sharedPrefs.getString(Constants.USER_TOKEN, "bad_token")
        assertThat(jwt, `is`("bad_token"))
    }

    @Test
    fun `getProfile with valid jwt`() = mainCoroutineRule.runBlockingTest {
        val result = repository.getProfile()

        assertThat(result.succeeded, `is`(true))

        result as Result.Success
        assertThat(result.data.email, `is`(TestConstants.USER_RESPONSE.user.email))
        assertThat(result.data.firstName, `is`(TestConstants.USER_RESPONSE.user.firstName))
        assertThat(result.data.lastName, `is`(TestConstants.USER_RESPONSE.user.lastName))
        assertThat(result.data.teams.entries,
            CoreMatchers.everyItem(IsIn(TestConstants.USER_RESPONSE.user.teams.entries))
        )
    }

    @Test
    fun `getProfile with invalid jwt`() = mainCoroutineRule.runBlockingTest {
        (dataSource as FakeUserDataSource).shouldReturnError = true
        val result = repository.getProfile()
        assertThat(result.succeeded, `is`(false))

        result as Result.Error
        assertThat(result.exception.message, `is`(TestConstants.LOGIN_ERROR))
    }
}