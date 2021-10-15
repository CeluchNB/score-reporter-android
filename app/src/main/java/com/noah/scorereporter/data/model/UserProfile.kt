package com.noah.scorereporter.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class UserProfile (
    @PrimaryKey
    @SerializedName("_id") val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val teams: Map<String, String>
): Parcelable