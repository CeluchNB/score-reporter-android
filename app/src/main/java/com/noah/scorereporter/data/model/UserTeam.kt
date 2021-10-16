package com.noah.scorereporter.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserTeam(
    val team: String,
    val role: Role
) : Parcelable