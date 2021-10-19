package com.noah.scorereporter.account

import android.content.SharedPreferences
import com.noah.scorereporter.data.model.UserProfile
import com.noah.scorereporter.data.network.Result
import com.noah.scorereporter.data.network.UserDataSource
import com.noah.scorereporter.data.network.UserNetworkError
import com.noah.scorereporter.data.network.succeeded
import com.noah.scorereporter.util.Constants
import javax.inject.Inject

class UserProfileRepository @Inject constructor(
    private val userRemoteDataSource: UserDataSource,
    private val sharedPrefs: SharedPreferences
) : IUserProfileRepository {

    override suspend fun login(email: String, password: String): UserProfile {
        return try {
            val result = userRemoteDataSource.login(email, password)
            sharedPrefs.edit().putString(Constants.USER_TOKEN, result.token).apply()
            result.user
        } catch (cause: Throwable) {
            throw cause
        }
    }

    override suspend fun getProfile(): UserProfile {
        val jwt = sharedPrefs.getString(Constants.USER_TOKEN, "")

        return try {
            userRemoteDataSource.getProfile(jwt ?: "")
        } catch (cause: Throwable) {
            throw cause
        }
    }

    override suspend fun signUp(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): UserProfile {
        return try {
            val result = userRemoteDataSource.signUp(firstName, lastName, email, password)

            sharedPrefs.edit().putString(Constants.USER_TOKEN, result.token).apply()
            result.user
        } catch (cause: Throwable) {
            throw cause
        }
    }

    override suspend fun logout() {
        try {
            val jwt = sharedPrefs.getString(Constants.USER_TOKEN, "")
            userRemoteDataSource.logout(jwt ?: "")
            sharedPrefs.edit().remove(Constants.USER_TOKEN).apply()
        } catch (cause: Throwable) {
            throw cause
        }
    }

    override fun hasSavedToken(): Boolean {
        return if (!sharedPrefs.contains(Constants.USER_TOKEN)) {
            false
        } else {
            val jwt = sharedPrefs.getString(Constants.USER_TOKEN, null)
            !(jwt == null || jwt.isEmpty())
        }
    }
}