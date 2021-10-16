package com.noah.scorereporter.account

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.noah.scorereporter.MainCoroutineRule
import com.noah.scorereporter.TestConstants
import com.noah.scorereporter.fake.FakeAndroidKeyStore
import com.noah.scorereporter.fake.FakeUserDataSource
import com.noah.scorereporter.data.network.Result
import com.noah.scorereporter.data.network.UserDataSource
import com.noah.scorereporter.data.network.succeeded
import com.noah.scorereporter.util.Constants
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.collection.IsIn
import org.junit.Before
import org.junit.BeforeClass
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
        assertThat(result.data.email, `is`(TestConstants.USER_RESPONSE_1.user.email))
        assertThat(result.data.firstName, `is`(TestConstants.USER_RESPONSE_1.user.firstName))
        assertThat(result.data.lastName, `is`(TestConstants.USER_RESPONSE_1.user.lastName))
        assertThat(result.data.teams, `is`(TestConstants.USER_RESPONSE_1.user.teams))

        val jwt = sharedPrefs.getString(Constants.USER_TOKEN, "bad_token")
        assertThat(jwt, `is`(not("bad_token")))
        assertThat(jwt, `is`(TestConstants.USER_RESPONSE_1.token))
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
        assertThat(result.data.email, `is`(TestConstants.USER_RESPONSE_1.user.email))
        assertThat(result.data.firstName, `is`(TestConstants.USER_RESPONSE_1.user.firstName))
        assertThat(result.data.lastName, `is`(TestConstants.USER_RESPONSE_1.user.lastName))
        assertThat(result.data.teams, `is`(TestConstants.USER_RESPONSE_1.user.teams))
    }

    @Test
    fun `getProfile with invalid jwt`() = mainCoroutineRule.runBlockingTest {
        (dataSource as FakeUserDataSource).shouldReturnError = true
        val result = repository.getProfile()
        assertThat(result.succeeded, `is`(false))

        result as Result.Error
        assertThat(result.exception.message, `is`(TestConstants.LOGIN_ERROR))
    }

    @Test
    fun `signUp with valid data`() = mainCoroutineRule.runBlockingTest {
        val result = repository.signUp(
            TestConstants.USER_PROFILE_1.firstName,
            TestConstants.USER_PROFILE_1.lastName,
            TestConstants.USER_PROFILE_1.email,
            "Pass12!"
        )
        assertThat(result.succeeded, `is`(true))

        result as Result.Success
        assertThat(result.data.firstName, `is`(TestConstants.USER_PROFILE_1.firstName))
        assertThat(result.data.lastName, `is`(TestConstants.USER_PROFILE_1.lastName))
        assertThat(result.data.email, `is`(TestConstants.USER_PROFILE_1.email))

        val jwt = sharedPrefs.getString(Constants.USER_TOKEN, "bad_token")
        assertThat(jwt, `is`(not("bad_token")))
        assertThat(jwt, `is`(TestConstants.USER_RESPONSE_1.token))
    }

    @Test
    fun `signUp with invalid data`() = mainCoroutineRule.runBlockingTest {
        (dataSource as FakeUserDataSource).shouldReturnError = true

        val result = repository.signUp(
            TestConstants.USER_PROFILE_1.firstName,
            TestConstants.USER_PROFILE_1.lastName,
            TestConstants.USER_PROFILE_1.email,
            "Pass12!"
        )
        assertThat(result.succeeded, `is`(false))
        val jwt = sharedPrefs.getString(Constants.USER_TOKEN, "bad_token")
        assertThat(jwt, `is`("bad_token"))
    }

    @Test
    fun `test successful logout`() = mainCoroutineRule.runBlockingTest {
        (dataSource as FakeUserDataSource).shouldReturnError = false
        repository.login("email@email.com", "password")
        assertThat(sharedPrefs.contains(Constants.USER_TOKEN), `is`(true))

        val result = repository.logout()
        assertThat(result.succeeded, `is`(true))
        assertThat(sharedPrefs.contains(Constants.USER_TOKEN), `is`(false))
    }

    @Test
    fun `test unsuccessful logout`() = mainCoroutineRule.runBlockingTest {
        repository.login("email@email.com", "password")
        assertThat(sharedPrefs.contains(Constants.USER_TOKEN), `is`(true))
        (dataSource as FakeUserDataSource).shouldReturnError = true

        val result = repository.logout()
        assertThat(result.succeeded, `is`(false))
        assertThat(sharedPrefs.contains(Constants.USER_TOKEN), `is`(true))

    }

    @Test
    fun `hasSavedToken without saved token`() {
        val value = repository.hasSavedToken()
        assertThat(value, `is`(false))
    }

    @Test
    fun `hasSavedToken with saved token`() = mainCoroutineRule.runBlockingTest {
        val result = repository.login("email@email.com", "password")
        assertThat(result.succeeded, `is`(true))

        val value = repository.hasSavedToken()
        assertThat(value, `is`(true))
    }

    companion object {
        @JvmStatic
        @BeforeClass
        fun beforeClass() {
            FakeAndroidKeyStore.setup
        }
    }
}