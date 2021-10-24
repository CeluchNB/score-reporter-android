package com.noah.scorereporter.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.util.*

@Entity
@Parcelize
data class Season(
    @PrimaryKey
    @SerializedName("_id") val id: String,
    val startDate: Date,
    val endDate: Date?,
    val owner: String,
    val games: List<SeasonGame>
) : Parcelable