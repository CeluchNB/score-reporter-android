package com.noah.scorereporter.account

import com.noah.scorereporter.network.Result
import com.noah.scorereporter.network.UserDataSource
import com.noah.scorereporter.network.UserService
import com.noah.scorereporter.network.model.LoginUser
import com.noah.scorereporter.network.model.User
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory

class UserProfileDataSource : UserDataSource {

    private var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://score-reporter.herokuapp.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private var service: UserService = retrofit.create(UserService::class.java)

    override suspend fun login(email: String, password: String): Result<User> {
//        val response = service.login(LoginUser(email, password)).awaitResponse()
//        if (response.isSuccessful) {
//            response.body()?.let {
//                return Result.Success(it)
//            }
//        }
//        return Result.Error(Exception(response.message()))
        TODO("Not yet implemented")
    }

    override suspend fun getProfile(jwt: String): Result<User> {
        TODO("Not yet implemented")
    }
}