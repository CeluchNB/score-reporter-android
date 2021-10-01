package com.noah.scorereporter.network.model

import com.noah.scorereporter.model.UserProfile

data class User (
    val user: UserProfile,
    val token: String
)