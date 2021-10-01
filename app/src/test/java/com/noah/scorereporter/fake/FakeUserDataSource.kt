package com.noah.scorereporter.fake

import com.noah.scorereporter.TestConstants
import com.noah.scorereporter.network.Result
import com.noah.scorereporter.network.UserDataSource
import com.noah.scorereporter.network.model.User

class FakeUserDataSource : UserDataSource {

    var shouldReturnError = false

    override suspend fun login(email: String, password: String): Result<User> {
        return if (email == "email@email.com") {
            Result.Success(TestConstants.USER_RESPONSE)
        } else {
            Result.Error(Exception(TestConstants.LOGIN_ERROR))
        }
    }

    override suspend fun getProfile(jwt: String): Result<User> {
        return if (!shouldReturnError) {
            Result.Success(TestConstants.USER_RESPONSE)
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
            Result.Success(TestConstants.USER_RESPONSE)
        } else {
            Result.Error(Exception(TestConstants.LOGIN_ERROR))
        }
    }
}