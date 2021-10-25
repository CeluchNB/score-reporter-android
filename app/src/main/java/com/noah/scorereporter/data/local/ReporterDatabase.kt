package com.noah.scorereporter.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.noah.scorereporter.data.model.Game
import com.noah.scorereporter.data.model.Season
import com.noah.scorereporter.data.model.Team
import com.noah.scorereporter.data.model.UserProfile
import com.noah.scorereporter.util.Converters

@Database(entities = [Team::class, Season::class, UserProfile::class, Game::class], version = 1)
@TypeConverters(Converters::class)
abstract class ReporterDatabase : RoomDatabase() {
    abstract fun teamDao(): TeamDao

    abstract fun seasonDao(): SeasonDao

    abstract fun userDao(): UserDao

    abstract fun gameDao(): GameDao
}