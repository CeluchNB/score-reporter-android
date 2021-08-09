package com.noah.scorereporter.account

import com.noah.scorereporter.model.UserProfile
import com.noah.scorereporter.network.Result
import com.noah.scorereporter.network.UserDataSource
import com.noah.scorereporter.network.UserService
import com.noah.scorereporter.network.model.User
import javax.inject.Inject

class UserProfileDataSource @Inject constructor(): UserDataSource {

    @Inject
    lateinit var service: UserService

    override suspend fun login(email: String, password: String): Result<User> {
//        val response = service.login(LoginUser(email, password)).awaitResponse()
//        if (response.isSuccessful) {
//            response.body()?.let {
//                return Result.Success(it)
//            }
//        }
//        return Result.Error(Exception(response.message()))
        return Result.Success(User(UserProfile("f", "l", "t@g.c", mapOf("one" to "two")), "123.asdf.1234"))
    }

    override suspend fun getProfile(jwt: String): Result<User> {
        TODO("Not yet implemented")
    }
}