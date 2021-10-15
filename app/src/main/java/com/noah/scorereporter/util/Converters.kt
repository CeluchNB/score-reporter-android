package com.noah.scorereporter.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.noah.scorereporter.data.model.Follower
import com.noah.scorereporter.data.model.TeamSeason
import java.util.*

class Converters {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun listOfFollowersToString(followers: List<Follower>?): String? {
        return Gson().toJson(followers)
    }

    @TypeConverter
    fun stringToListOfFollowers(value: String?): List<Follower>? {
        return Gson().fromJson(value, object : TypeToken<List<Follower>>() {}.type)
    }

    @TypeConverter
    fun listOfStringToString(strings: List<String>?): String? {
        return Gson().toJson(strings)
    }

    @TypeConverter
    fun stringToListOfStrings(value: String?): List<String>? {
        return Gson().fromJson(value, object : TypeToken<List<String>>() {}.type)
    }

    @TypeConverter
    fun listOfTeamSeasonsToString(seasons: List<TeamSeason>?): String? {
        return Gson().toJson(seasons)
    }

    @TypeConverter
    fun stringToListOfTeamSeasons(value: String?) : List<TeamSeason>? {
        return Gson().fromJson(value, object : TypeToken<List<TeamSeason>>() {}.type)
    }
}