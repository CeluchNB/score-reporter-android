package com.noah.scorereporter.account

import android.content.Context
import com.noah.scorereporter.model.UserProfile
import com.noah.scorereporter.network.Result
import com.noah.scorereporter.network.UserDataSource
import com.noah.scorereporter.network.succeeded
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class UserProfileRepository @Inject constructor(@ApplicationContext private val context: Context,
                                                private val userRemoteDataSource: UserDataSource) : IUserProfileRepository {

    override suspend fun login(email: String, password: String): Result<UserProfile> {
        val result = userRemoteDataSource.login(email, password)

        return if (result.succeeded) {
            val sharedPrefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
            result as Result.Success
            sharedPrefs.edit().putString("jwt_token", result.data.token).apply()
            Result.Success(result.data.user)
        } else {
            Result.Error((result as Result.Error).exception)
        }
    }

    override suspend fun getProfile(): Result<UserProfile> {
        val sharedPrefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val jwt = sharedPrefs.getString("jwt_token", "")

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