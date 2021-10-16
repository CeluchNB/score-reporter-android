package com.noah.scorereporter.fake

import com.noah.scorereporter.TestConstants
import com.noah.scorereporter.data.model.UserProfile
import com.noah.scorereporter.data.network.Result
import com.noah.scorereporter.data.network.UserDataSource
import com.noah.scorereporter.data.model.User

class FakeUserDataSource : UserDataSource {

    var shouldReturnError = false

    override suspend fun login(email: String, password: String): Result<User> {
        return if (email == "email@email.com") {
            Result.Success(TestConstants.USER_RESPONSE_1)
        } else {
            Result.Error(Exception(TestConstants.LOGIN_ERROR))
        }
    }

    override suspend fun getProfile(jwt: String): Result<UserProfile> {
        return if (!shouldReturnError) {
            Result.Success(TestConstants.USER_RESPONSE_1.user)
        } else {
            Result.Error(Exception(TestConstants.LOGIN_ERROR))
        }
    }

    override suspend fun signUp(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): Result<User> {
        return if (!shouldReturnError) {
            Result.Success(TestConstants.USER_RESPONSE_1)
        } else {
            Result.Error(Exception(TestConstants.LOGIN_ERROR))
        }
    }

    override suspend fun logout(jwt: String): Result<Boolean> {
        return if (!shouldReturnError) {
            return Result.Success(true)
        } else {
            Result.Error(java.lang.Exception(TestConstants.LOGIN_ERROR))
        }
    }
}