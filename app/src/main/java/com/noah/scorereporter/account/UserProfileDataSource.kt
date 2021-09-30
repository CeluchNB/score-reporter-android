package com.noah.scorereporter.account

import com.noah.scorereporter.network.Result
import com.noah.scorereporter.network.UserDataSource
import com.noah.scorereporter.network.UserService
import com.noah.scorereporter.network.model.LoginUser
import com.noah.scorereporter.network.model.User
import retrofit2.awaitResponse
import javax.inject.Inject

class UserProfileDataSource @Inject constructor(): UserDataSource {

    @Inject
    lateinit var service: UserService

    override suspend fun login(email: String, password: String): Result<User> {
        val response = service.login(LoginUser(email, password)).awaitResponse()
        if (response.isSuccessful) {
            response.body()?.let {
                return Result.Success(it)
            }
        }
        return Result.Error(Exception(response.message()))
    }

    override suspend fun getProfile(jwt: String): Result<User> {
        TODO("Not yet implemented")
    }

    override suspend fun signUp(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): Result<User> {
        TODO("Not yet implemented")
    }
}