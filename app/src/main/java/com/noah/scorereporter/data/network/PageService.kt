package com.noah.scorereporter.data.network

import com.noah.scorereporter.data.model.Season
import com.noah.scorereporter.data.model.Team
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface PageService {
    @GET("/team/{id}")
    fun getTeamById(@Path("id") id: String): Call<Team>

    @PATCH("/team/{id}/follow")
    fun followTeam(@Path("id") id: String): Call<Team>

    @GET("/season/{id}")
    fun getSeasonById(@Path("id") id: String): Call<Season>
}