package com.noah.scorereporter.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SeasonGame(
    @SerializedName("_id")
    val id: String,
    val game: String
) : Parcelable
