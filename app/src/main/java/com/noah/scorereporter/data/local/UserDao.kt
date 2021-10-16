package com.noah.scorereporter.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.noah.scorereporter.data.model.UserProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = REPLACE)
    fun save(users: List<UserProfile>)

    @Query("SELECT * FROM userprofile WHERE id = :id")
    fun getUserById(id: String): Flow<UserProfile>

    @Query("SELECT EXISTS(SELECT * FROM userprofile WHERE id = :id)")
    fun hasUser(id: String): Boolean
}