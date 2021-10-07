package com.noah.scorereporter.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Follower(
    val user: String,
    val role: Role
): Parcelable
