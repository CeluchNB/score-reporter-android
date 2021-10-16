package com.noah.scorereporter.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.noah.scorereporter.data.model.TeamFollower
import com.noah.scorereporter.data.model.TeamSeason
import com.noah.scorereporter.data.model.UserTeam
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
    fun listOfFollowersToString(teamFollowers: List<TeamFollower>?): String? {
        return Gson().toJson(teamFollowers)
    }

    @TypeConverter
    fun stringToListOfFollowers(value: String?): List<TeamFollower>? {
        return Gson().fromJson(value, object : TypeToken<List<TeamFollower>>() {}.type)
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
    fun stringToListOfTeamSeasons(value: String?): List<TeamSeason>? {
        return Gson().fromJson(value, object : TypeToken<List<TeamSeason>>() {}.type)
    }

    @TypeConverter
    fun mapOfStringsToString(map: Map<String, String>): String? {
        return Gson().toJson(map)
    }

    @TypeConverter
    fun stringToMapOfStrings(value: String?): Map<String, String>? {
        return Gson().fromJson(value, object : TypeToken<Map<String, String>>() {}.type)
    }

    @TypeConverter
    fun listOfUserTeamToString(seasons: List<UserTeam>?): String? {
        return Gson().toJson(seasons)
    }

    @TypeConverter
    fun stringToListOfUserTeams(value: String?): List<UserTeam>? {
        return Gson().fromJson(value, object : TypeToken<List<UserTeam>>() {}.type)
    }
}