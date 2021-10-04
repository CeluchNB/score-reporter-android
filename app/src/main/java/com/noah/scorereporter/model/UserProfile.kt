package com.noah.scorereporter.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserProfile (
    val firstName: String,
    val lastName: String,
    val email: String,
    val teams: Map<String, String>
): Parcelable