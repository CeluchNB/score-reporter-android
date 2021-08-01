package com.noah.scorereporter.account

import com.noah.scorereporter.model.UserProfile
import com.noah.scorereporter.network.Result
import com.noah.scorereporter.network.UserDataSource

class UserProfileRepository(private val userRemoteDataSource: UserDataSource) : AccountRepository {

    override suspend fun login(email: String, password: String): Result<UserProfile> {
        TODO("Not yet implemented")
    }

    override suspend fun getProfile(): Result<UserProfile> {
        TODO("Not yet implemented")
    }
}