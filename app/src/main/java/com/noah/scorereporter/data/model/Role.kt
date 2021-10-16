package com.noah.scorereporter.data.model

import com.google.gson.annotations.SerializedName

enum class Role(role: String) {
    @SerializedName("Fan")
    FAN("Fan"),
    @SerializedName("Coach")
    COACH("Coach"),
    @SerializedName("Player")
    PLAYER("Player")
}