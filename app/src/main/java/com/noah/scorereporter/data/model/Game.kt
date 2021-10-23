package com.noah.scorereporter.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Game(
    @SerializedName("_id")
    val id: String,
    val season: String,
    val awayTeam: String,
    val homeTeam: String,
    val innings: GameInnings,
    val winner: String,
    val date: Date
) : Parcelable
