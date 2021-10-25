package com.noah.scorereporter.data.network

import com.noah.scorereporter.data.model.UserProfile
import com.noah.scorereporter.data.model.LoginUser
import com.noah.scorereporter.data.model.SignUpUser
import com.noah.scorereporter.data.model.User
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface UserService {
    @POST("/user/login")
    suspend fun login(@Body user: LoginUser): User

    @GET("/user/profile")
    suspend fun getProfile(@Header("Authorization") jwt: String): UserProfile

    @POST("/user")
    suspend fun signup(@Body user: SignUpUser): User

    @POST("/user/logout")
    suspend fun logout(@Header("Authorization") jwt: String): ResponseBody
}