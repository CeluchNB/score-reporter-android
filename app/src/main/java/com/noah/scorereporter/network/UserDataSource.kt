package com.noah.scorereporter.network

import com.noah.scorereporter.network.model.User

interface UserDataSource {

    suspend fun login(email: String, password: String): Result<User>

    suspend fun getProfile(jwt: String): Result<User>
}