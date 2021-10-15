package com.noah.scorereporter.account

import com.noah.scorereporter.data.model.UserProfile
import com.noah.scorereporter.data.network.Result
import com.noah.scorereporter.data.network.UserDataSource
import com.noah.scorereporter.data.network.UserService
import com.noah.scorereporter.data.model.LoginUser
import com.noah.scorereporter.data.model.SignUpUser
import com.noah.scorereporter.data.model.User
import retrofit2.HttpException
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

    override suspend fun getProfile(jwt: String): Result<UserProfile> {
        val response = service.getProfile("Bearer $jwt").awaitResponse()
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

    override suspend fun logout(jwt: String): Result<Boolean> {
        return try {
            val response = service.logout("Bearer $jwt").awaitResponse()

            if (response.code() == 200) {
                Result.Success(true)
            } else {
                throw HttpException(response)
            }
        } catch (exception: HttpException) {
            Result.Error(Exception(exception.message()))
        }
    }

    private fun <T> unwrapUserResult(response: Response<T>): Result<T> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Result.Success(it)
            }
        }
        return Result.Error(Exception(response.message()))
    }
}