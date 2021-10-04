package com.noah.scorereporter.network

import com.noah.scorereporter.model.UserProfile
import com.noah.scorereporter.network.model.LoginUser
import com.noah.scorereporter.network.model.SignUpUser
import com.noah.scorereporter.network.model.User
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface UserService {
    @POST("/user/login")
    fun login(@Body user: LoginUser): Call<User>

    @GET("/user/profile")
    fun getProfile(@Header("Authorization") jwt: String): Call<UserProfile>

    @POST("/user")
    fun signup(@Body user: SignUpUser): Call<User>

    @POST("/user/logout")
    fun logout(@Header("Authorization") jwt: String): Call<ResponseBody>
}