package com.noah.scorereporter.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Season(
    @SerializedName("_id") val id: String,
    val startDate: Date,
    val endDate: Date?,
    val owner: String
) : Parcelable