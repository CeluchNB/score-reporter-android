package com.noah.scorereporter.account

import android.content.SharedPreferences
import com.noah.scorereporter.model.UserProfile
import com.noah.scorereporter.network.Result
import com.noah.scorereporter.network.UserDataSource
import com.noah.scorereporter.network.succeeded
import com.noah.scorereporter.util.Constants
import javax.inject.Inject

class UserProfileRepository @Inject constructor(
    private val userRemoteDataSource: UserDataSource,
    private val sharedPrefs: SharedPreferences
) : IUserProfileRepository {

    override suspend fun login(email: String, password: String): Result<UserProfile> {
        val result = userRemoteDataSource.login(email, password)

        return if (result.succeeded) {
            result as Result.Success
            sharedPrefs.edit().putString(Constants.USER_TOKEN, result.data.token).apply()
            Result.Success(result.data.user)
        } else {
            Result.Error((result as Result.Error).exception)
        }
    }

    override suspend fun getProfile(): Result<UserProfile> {
        val jwt = sharedPrefs.getString(Constants.USER_TOKEN, "")

        val result = userRemoteDataSource.getProfile(jwt ?: "")
        return if (result.succeeded) {
            result as Result.Success
            Result.Success(result.data.user)
        } else {
            result as Result.Error
            Result.Error(result.exception)
        }
    }
}