package com.noah.scorereporter.fake

import com.noah.scorereporter.AndroidTestConstants
import com.noah.scorereporter.account.IUserProfileRepository
import com.noah.scorereporter.model.UserProfile
import com.noah.scorereporter.data.network.Result
import kotlinx.coroutines.delay
import javax.inject.Inject

class AndroidFakeUserRepository @Inject constructor(): IUserProfileRepository {

    var valid = true

    override suspend fun login(email: String, password: String): Result<UserProfile> {
        delay(1000)
        return if (email == "email@email.com") {
            Result.Success(AndroidTestConstants.USER_PROFILE)
        } else {
            Result.Error(Exception(AndroidTestConstants.LOGIN_ERROR))
        }
    }

    override suspend fun getProfile(): Result<UserProfile> {
        return if (valid) {
            Result.Success(AndroidTestConstants.USER_PROFILE)
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
        delay(1000)
        return if (email != "invalid@email.com") {
            Result.Success(AndroidTestConstants.USER_PROFILE)
        } else {
            Result.Error(Exception(AndroidTestConstants.LOGIN_ERROR))
        }
    }

    override suspend fun logout(): Result<Boolean> {
        delay(1000)
        return if (valid) {
            Result.Success(true)
        } else {
            Result.Error(Exception(AndroidTestConstants.LOGIN_ERROR))
        }
    }

    override fun hasSavedToken(): Boolean {
        return valid
    }
}