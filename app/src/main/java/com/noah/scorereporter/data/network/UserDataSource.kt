package com.noah.scorereporter.data.network

import com.noah.scorereporter.data.model.UserProfile
import com.noah.scorereporter.data.model.User

interface UserDataSource {

    suspend fun login(email: String, password: String): Result<User>

    suspend fun getProfile(jwt: String): Result<UserProfile>

    suspend fun signUp(firstName: String, lastName: String, email: String, password: String): Result<User>

    suspend fun logout(jwt: String): Result<Boolean>
}