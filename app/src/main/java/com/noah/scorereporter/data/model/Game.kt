package com.noah.scorereporter.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.util.*

@Entity
@Parcelize
data class Game(
    @PrimaryKey
    @SerializedName("_id") val id: String,
    val season: String,
    val awayTeam: String,
    val homeTeam: String,
    val innings: GameInnings,
    val winner: String,
    val date: Date
) : Parcelable
