package com.noah.scorereporter.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.noah.scorereporter.data.model.Game
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {

    @Insert(onConflict = REPLACE)
    suspend fun save(vararg games: Game)

    @Query("SELECT * FROM game WHERE :id = id")
    fun getGameById(id: String): Flow<Game>

    @Query("SELECT EXISTS(SELECT * FROM game WHERE :id = id)")
    fun hasGame(id: String): Boolean
}