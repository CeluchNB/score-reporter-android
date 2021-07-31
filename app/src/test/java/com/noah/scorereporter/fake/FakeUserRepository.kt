package com.noah.scorereporter.fake

import com.noah.scorereporter.Constants
import com.noah.scorereporter.account.UserRepository
import com.noah.scorereporter.model.UserProfile
import com.noah.scorereporter.network.Result

class FakeUserRepository : UserRepository {

    override suspend fun login(email: String, password: String): Result<UserProfile> {
        return if (email == "email@email.com") {
            Result.Success(Constants.USER_PROFILE)
        } else {
            Result.Error(Exception(Constants.LOGIN_ERROR))
        }
    }

    override suspend fun getProfile(): Result<UserProfile> {
        return Result.Success(Constants.USER_PROFILE)
    }
}