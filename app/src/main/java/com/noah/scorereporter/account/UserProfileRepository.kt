package com.noah.scorereporter.account

import com.noah.scorereporter.model.UserProfile
import com.noah.scorereporter.network.Result

class UserProfileRepository : UserRepository {

    override suspend fun login(email: String, password: String): Result<UserProfile> {
        TODO("Not yet implemented")
    }

    override suspend fun getProfile(jwt: String): Result<UserProfile> {
        TODO("Not yet implemented")
    }
}