package com.noah.scorereporter.network

import com.noah.scorereporter.network.model.LoginUser
import com.noah.scorereporter.network.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {
    @POST("/user/login")
    fun login(@Body user: LoginUser) : Call<User>

}