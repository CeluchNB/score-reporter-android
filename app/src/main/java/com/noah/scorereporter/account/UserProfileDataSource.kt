package com.noah.scorereporter.account

import com.noah.scorereporter.data.model.UserProfile
import com.noah.scorereporter.data.network.Result
import com.noah.scorereporter.data.network.UserDataSource
import com.noah.scorereporter.data.network.UserService
import com.noah.scorereporter.data.model.LoginUser
import com.noah.scorereporter.data.model.SignUpUser
import com.noah.scorereporter.data.model.User
import com.noah.scorereporter.data.network.UserNetworkError
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.awaitResponse
import javax.inject.Inject

class UserProfileDataSource @Inject constructor(): UserDataSource {

    @Inject
    lateinit var service: UserService

    override suspend fun login(email: String, password: String): User {
        return try {
            service.login(LoginUser(email, password))
        } catch (throwable: Throwable) {
            throw UserNetworkError("Unable to login user", throwable)
        }
    }

    override suspend fun getProfile(jwt: String): UserProfile {
        return try {
            service.getProfile("Bearer $jwt")
        } catch (throwable: Throwable) {
            throw UserNetworkError("Unable to get user profile", throwable)
        }
    }

    override suspend fun signUp(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): User {
        return try {
            service.signup(SignUpUser(firstName, lastName, email, password))
        } catch (throwable: Throwable) {
            throw UserNetworkError("Unable to sign up", throwable)
        }
    }

    override suspend fun logout(jwt: String) {
        try {
            service.logout("Bearer $jwt")
        } catch (throwable: Throwable) {
            throw UserNetworkError("Unable to logout", throwable)
        }
    }
}