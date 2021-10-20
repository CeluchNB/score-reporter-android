package com.noah.scorereporter.fake

import com.noah.scorereporter.AndroidTestConstants
import com.noah.scorereporter.account.IUserProfileRepository
import com.noah.scorereporter.data.model.UserProfile
import com.noah.scorereporter.data.network.Result
import com.noah.scorereporter.data.network.UserNetworkError
import kotlinx.coroutines.delay
import javax.inject.Inject

class AndroidFakeUserRepository @Inject constructor(): IUserProfileRepository {

    var valid = true

    override suspend fun login(email: String, password: String): UserProfile {
        delay(1000)
        return if (email == "email@email.com") {
            AndroidTestConstants.USER_PROFILE
        } else {
            throw UserNetworkError(AndroidTestConstants.LOGIN_ERROR, Throwable())
        }
    }

    override suspend fun getProfile(): UserProfile {
        return if (valid) {
            AndroidTestConstants.USER_PROFILE
        } else {
            throw UserNetworkError(AndroidTestConstants.LOGIN_ERROR, Throwable())
        }
    }

    override suspend fun signUp(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): UserProfile {
        delay(1000)
        return if (email != "invalid@email.com") {
            AndroidTestConstants.USER_PROFILE
        } else {
            throw UserNetworkError(AndroidTestConstants.LOGIN_ERROR, Throwable())
        }
    }

    override suspend fun logout() {
        delay(1000)
       if (!valid) {
           throw UserNetworkError(AndroidTestConstants.LOGIN_ERROR, Throwable())
        }
    }

    override fun hasSavedToken(): Boolean {
        return valid
    }
}