package com.noah.scorereporter.fake

import com.noah.scorereporter.TestConstants
import com.noah.scorereporter.account.IUserProfileRepository
import com.noah.scorereporter.model.UserProfile
import com.noah.scorereporter.network.Result

class FakeUserRepository : IUserProfileRepository {

    override suspend fun login(email: String, password: String): Result<UserProfile> {
        return if (email == "email@email.com") {
            Result.Success(TestConstants.USER_PROFILE)
        } else {
            Result.Error(Exception(TestConstants.LOGIN_ERROR))
        }
    }

    override suspend fun getProfile(): Result<UserProfile> {
        return Result.Success(TestConstants.USER_PROFILE)
    }
}