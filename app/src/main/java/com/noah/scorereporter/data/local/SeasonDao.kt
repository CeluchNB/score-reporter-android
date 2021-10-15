package com.noah.scorereporter.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.noah.scorereporter.data.model.Season
import kotlinx.coroutines.flow.Flow

@Dao
interface SeasonDao {

    @Insert(onConflict = REPLACE)
    fun save(seasons: List<Season>)

    @Query("SELECT * FROM season WHERE id = :id")
    fun getSeasonById(id: String): Flow<Season>

    @Query("SELECT EXISTS(SELECT * FROM season WHERE id = :id)")
    fun hasSeason(id: String): Boolean
}