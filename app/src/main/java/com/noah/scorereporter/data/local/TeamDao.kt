package com.noah.scorereporter.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.noah.scorereporter.data.model.Team

@Dao
interface TeamDao {

    @Insert( onConflict = REPLACE )
    fun save(teams: List<Team>)

    @Query("SELECT * FROM team WHERE id = :id")
    fun getTeamById(id: String): Team
}