package com.noah.scorereporter.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GameInnings(
    val away: List<Int>,
    val home: List<Int>
) : Parcelable
