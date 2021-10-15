package com.noah.scorereporter.data.model

import java.util.*

data class CreateTeam(
    val name: String,
    val founded: Date,
    val ownerRole: Role
)
