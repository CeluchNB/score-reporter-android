package com.noah.scorereporter.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.util.*

@Entity
@Parcelize
data class Team(
    @SerializedName("_id")
    @PrimaryKey val id: String,
    val name: String,
    val founded: Date,
    val ended: Date?,
    val owner: String,
    val followers: List<Follower>,
    val seasons: List<TeamSeason>
) : Parcelable
