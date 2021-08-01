package com.noah.scorereporter.account

import com.noah.scorereporter.model.UserProfile
import com.noah.scorereporter.network.Result

interface AccountRepository {
    suspend fun login(email: String, password: String) : Result<UserProfile>

    suspend fun getProfile() : Result<UserProfile>
}