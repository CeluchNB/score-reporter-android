package com.noah.scorereporter.data.network

import com.noah.scorereporter.data.model.Season
import com.noah.scorereporter.data.model.Team
import com.noah.scorereporter.data.model.UserProfile
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface PageService {
    @GET("/team/{id}")
    suspend fun getTeamById(@Path("id") id: String): Team

    @PATCH("/team/{id}/follow")
    suspend fun followTeam(@Path("id") id: String): Team

    @GET("/season/{id}")
    suspend fun getSeasonById(@Path("id") id: String): Season

    @GET("/user/{id}")
    suspend fun getUserById(@Path("id") id: String): UserProfile
}