package com.noah.scorereporter.fake

import com.noah.scorereporter.TestConstants
import com.noah.scorereporter.account.IUserProfileRepository
import com.noah.scorereporter.model.UserProfile
import com.noah.scorereporter.network.Result

class FakeUserRepository : IUserProfileRepository {

    var validToken = true

    override suspend fun login(email: String, password: String): Result<UserProfile> {
        return if (email == "email@email.com") {
            Result.Success(TestConstants.USER_PROFILE)
        } else {
            Result.Error(Exception(TestConstants.LOGIN_ERROR))
        }
    }

    override suspend fun getProfile(): Result<UserProfile> {
        return if (validToken) {
            Result.Success(TestConstants.USER_PROFILE)
        } else {
            Result.Error(Exception("Error"))
        }
    }

    override suspend fun signUp(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): Result<UserProfile> {
        return if (email != "invalid@email.com") {
            Result.Success(TestConstants.USER_PROFILE)
        } else {
            Result.Error(Exception(TestConstants.LOGIN_ERROR))
        }
    }
}