package com.noah.scorereporter.fake

import com.noah.scorereporter.TestConstants
import com.noah.scorereporter.account.IUserProfileRepository
import com.noah.scorereporter.data.model.UserProfile
import com.noah.scorereporter.data.network.Result
import com.noah.scorereporter.data.network.UserNetworkError

class FakeUserRepository : IUserProfileRepository {

    var valid = true

    override suspend fun login(email: String, password: String): UserProfile {
        return if (email == "email@email.com") {
            TestConstants.USER_PROFILE_1
        } else {
            throw UserNetworkError(TestConstants.LOGIN_ERROR, null)
        }
    }

    override suspend fun getProfile(): UserProfile {
        return if (valid) {
            TestConstants.USER_PROFILE_1
        } else {
            throw UserNetworkError(TestConstants.LOGIN_ERROR, null)
        }
    }

    override suspend fun signUp(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): UserProfile {
        return if (email != "invalid@email.com") {
            TestConstants.USER_PROFILE_1
        } else {
            throw UserNetworkError(TestConstants.LOGIN_ERROR, null)
        }
    }

    override suspend fun logout() {
        if (!valid) {
            throw UserNetworkError(TestConstants.LOGIN_ERROR, null)
        }
    }

    override fun hasSavedToken(): Boolean {
        return valid
    }
}