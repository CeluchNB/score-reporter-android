package com.noah.scorereporter.account

import com.noah.scorereporter.data.model.UserProfile
import com.noah.scorereporter.data.network.Result

interface IUserProfileRepository {
    suspend fun login(email: String, password: String) : UserProfile

    suspend fun getProfile() : UserProfile

    suspend fun signUp(firstName: String, lastName: String, email: String, password: String): UserProfile

    suspend fun logout()

    fun hasSavedToken(): Boolean
}