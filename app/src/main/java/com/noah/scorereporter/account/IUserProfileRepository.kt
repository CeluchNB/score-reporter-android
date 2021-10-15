package com.noah.scorereporter.account

import com.noah.scorereporter.model.UserProfile
import com.noah.scorereporter.data.network.Result

interface IUserProfileRepository {
    suspend fun login(email: String, password: String) : Result<UserProfile>

    suspend fun getProfile() : Result<UserProfile>

    suspend fun signUp(firstName: String, lastName: String, email: String, password: String): Result<UserProfile>

    suspend fun logout(): Result<Boolean>

    fun hasSavedToken(): Boolean
}