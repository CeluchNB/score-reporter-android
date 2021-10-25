package com.noah.scorereporter.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.noah.scorereporter.data.model.Team
import kotlinx.coroutines.flow.Flow

@Dao
interface TeamDao {

    @Insert( onConflict = REPLACE )
    suspend fun save(vararg team: Team)

    @Query("SELECT * FROM team WHERE id = :id")
    fun getTeamById(id: String): Flow<Team>

    @Query("SELECT EXISTS(SELECT * FROM team WHERE id = :id)")
    fun hasTeam(id: String): Boolean
}