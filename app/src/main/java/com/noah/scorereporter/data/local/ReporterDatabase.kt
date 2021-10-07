package com.noah.scorereporter.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.noah.scorereporter.data.model.Team
import com.noah.scorereporter.util.Converters

@Database(entities = [Team::class], version = 1)
@TypeConverters(Converters::class)
abstract class ReporterDatabase : RoomDatabase() {
    abstract fun teamDao(): TeamDao
}