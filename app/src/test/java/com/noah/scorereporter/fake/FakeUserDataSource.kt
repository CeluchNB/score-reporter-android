package com.noah.scorereporter.fake

import com.noah.scorereporter.TestConstants
import com.noah.scorereporter.data.model.UserProfile
import com.noah.scorereporter.data.network.Result
import com.noah.scorereporter.data.network.UserDataSource
import com.noah.scorereporter.data.model.User
import com.noah.scorereporter.data.network.UserNetworkError

class FakeUserDataSource : UserDataSource {

    var shouldReturnError = false

    override suspend fun login(email: String, password: String): User {
        return if (email == "email@email.com") {
            TestConstants.USER_RESPONSE_1
        } else {
            throw UserNetworkError(TestConstants.LOGIN_ERROR, null)
        }
    }

    override suspend fun getProfile(jwt: String): UserProfile {
        return if (!shouldReturnError) {
            TestConstants.USER_RESPONSE_1.user
        } else {
            throw UserNetworkError(TestConstants.LOGIN_ERROR, null)
        }
    }

    override suspend fun signUp(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): User {
        return if (!shouldReturnError) {
            TestConstants.USER_RESPONSE_1
        } else {
            throw UserNetworkError(TestConstants.LOGIN_ERROR, null)
        }
    }

    override suspend fun logout(jwt: String) {
        if (shouldReturnError) {
            throw UserNetworkError(TestConstants.LOGIN_ERROR, null)
        }
    }
}