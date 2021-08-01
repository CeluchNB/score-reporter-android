package com.noah.scorereporter.account

import android.content.Context
import com.noah.scorereporter.model.UserProfile
import com.noah.scorereporter.network.Result
import com.noah.scorereporter.network.UserDataSource
import com.noah.scorereporter.network.model.User
import com.noah.scorereporter.network.model.asUserProfile
import com.noah.scorereporter.network.succeeded

class UserProfileRepository(private val context: Context,
                            private val userRemoteDataSource: UserDataSource) : AccountRepository {

    override suspend fun login(email: String, password: String): Result<UserProfile> {
        val result = userRemoteDataSource.login(email, password)

        return if (result.succeeded) {
            val sharedPrefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
            result as Result.Success
            sharedPrefs.edit().putString("jwt_token", result.data.jwt).apply()
            Result.Success(result.data.asUserProfile())
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
            Result.Success(result.data.asUserProfile())
        } else {
            result as Result.Error
            Result.Error(result.exception)
        }
    }
}