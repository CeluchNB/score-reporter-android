package com.noah.scorereporter.pages.model

import com.noah.scorereporter.data.model.Role

data class Follower(
    val firstName: String,
    val lastName: String,
    val email: String,
    val role: Role
)