package com.noah.scorereporter.account

import com.noah.scorereporter.network.Result
import com.noah.scorereporter.network.UserDataSource
import com.noah.scorereporter.network.UserService
import com.noah.scorereporter.network.model.LoginUser
import com.noah.scorereporter.network.model.SignUpUser
import com.noah.scorereporter.network.model.User
import retrofit2.Response
import retrofit2.awaitResponse
import javax.inject.Inject

class UserProfileDataSource @Inject constructor(): UserDataSource {

    @Inject
    lateinit var service: UserService

    override suspend fun login(email: String, password: String): Result<User> {
        val response = service.login(LoginUser(email, password)).awaitResponse()
        return unwrapUserResult(response)
    }

    override suspend fun getProfile(jwt: String): Result<User> {
        val response = service.getProfile(jwt).awaitResponse()
        return unwrapUserResult(response)
    }

    override suspend fun signUp(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): Result<User> {
        val response = service.signup(SignUpUser(firstName, lastName, email, password)).awaitResponse()
        return unwrapUserResult(response)
    }

    private fun unwrapUserResult(response: Response<User>): Result<User> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Result.Success(it)
            }
        }
        return Result.Error(Exception(response.message()))
    }
}