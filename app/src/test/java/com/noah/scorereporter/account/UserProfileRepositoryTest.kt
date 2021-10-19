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
import com.noah.scorereporter.data.network.UserDataSource
import com.noah.scorereporter.data.network.UserNetworkError
import com.noah.scorereporter.util.Constants
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
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

        assertThat(result.email, `is`(TestConstants.USER_RESPONSE_1.user.email))
        assertThat(result.firstName, `is`(TestConstants.USER_RESPONSE_1.user.firstName))
        assertThat(result.lastName, `is`(TestConstants.USER_RESPONSE_1.user.lastName))
        assertThat(result.teams, `is`(TestConstants.USER_RESPONSE_1.user.teams))

        val jwt = sharedPrefs.getString(Constants.USER_TOKEN, "bad_token")
        assertThat(jwt, `is`(not("bad_token")))
        assertThat(jwt, `is`(TestConstants.USER_RESPONSE_1.token))
    }

    @Test(expected = UserNetworkError::class)
    fun `login with invalid email`() = mainCoroutineRule.runBlockingTest {
        repository.login("invalid@email.com", "password")

        val jwt = sharedPrefs.getString(Constants.USER_TOKEN, "bad_token")
        assertThat(jwt, `is`("bad_token"))
    }

    @Test
    fun `getProfile with valid jwt`() = mainCoroutineRule.runBlockingTest {
        val result = repository.getProfile()

        assertThat(result.email, `is`(TestConstants.USER_RESPONSE_1.user.email))
        assertThat(result.firstName, `is`(TestConstants.USER_RESPONSE_1.user.firstName))
        assertThat(result.lastName, `is`(TestConstants.USER_RESPONSE_1.user.lastName))
        assertThat(result.teams, `is`(TestConstants.USER_RESPONSE_1.user.teams))
    }

    @Test(expected = UserNetworkError::class)
    fun `getProfile with invalid jwt`() = mainCoroutineRule.runBlockingTest {
        (dataSource as FakeUserDataSource).shouldReturnError = true
        repository.getProfile()
    }

    @Test
    fun `signUp with valid data`() = mainCoroutineRule.runBlockingTest {
        val result = repository.signUp(
            TestConstants.USER_PROFILE_1.firstName,
            TestConstants.USER_PROFILE_1.lastName,
            TestConstants.USER_PROFILE_1.email,
            "Pass12!"
        )

        assertThat(result.firstName, `is`(TestConstants.USER_PROFILE_1.firstName))
        assertThat(result.lastName, `is`(TestConstants.USER_PROFILE_1.lastName))
        assertThat(result.email, `is`(TestConstants.USER_PROFILE_1.email))

        val jwt = sharedPrefs.getString(Constants.USER_TOKEN, "bad_token")
        assertThat(jwt, `is`(not("bad_token")))
        assertThat(jwt, `is`(TestConstants.USER_RESPONSE_1.token))
    }

    @Test
    fun `signUp with invalid data`() = mainCoroutineRule.runBlockingTest {
        (dataSource as FakeUserDataSource).shouldReturnError = true

        try {
            repository.signUp(
                TestConstants.USER_PROFILE_1.firstName,
                TestConstants.USER_PROFILE_1.lastName,
                TestConstants.USER_PROFILE_1.email,
                "Pass12!"
            )
        } catch (exception: UserNetworkError) {
            assertThat(exception.message, `is`(TestConstants.LOGIN_ERROR))
        } finally {
            val jwt = sharedPrefs.getString(Constants.USER_TOKEN, "bad_token")
            assertThat(jwt, `is`("bad_token"))
        }
    }

    @Test
    fun `test successful logout`() = mainCoroutineRule.runBlockingTest {
        (dataSource as FakeUserDataSource).shouldReturnError = false
        repository.login("email@email.com", "password")
        assertThat(sharedPrefs.contains(Constants.USER_TOKEN), `is`(true))

        repository.logout()
        assertThat(sharedPrefs.contains(Constants.USER_TOKEN), `is`(false))
    }

    @Test
    fun `test unsuccessful logout`() = mainCoroutineRule.runBlockingTest {
        repository.login("email@email.com", "password")
        assertThat(sharedPrefs.contains(Constants.USER_TOKEN), `is`(true))
        (dataSource as FakeUserDataSource).shouldReturnError = true

        try {
            repository.logout()
        } catch (exception: UserNetworkError) {
            assertThat(exception.message, `is`(TestConstants.LOGIN_ERROR))
        } finally {
            assertThat(sharedPrefs.contains(Constants.USER_TOKEN), `is`(true))
        }
    }

    @Test
    fun `hasSavedToken without saved token`() {
        val value = repository.hasSavedToken()
        assertThat(value, `is`(false))
    }

    @Test
    fun `hasSavedToken with saved token`() = mainCoroutineRule.runBlockingTest {
        repository.login("email@email.com", "password")

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