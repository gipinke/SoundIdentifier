package com.tcc.soundidentifier.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tcc.soundidentifier.database.table.ClassifiedSoundsTableModel

@Dao
interface ClassifiedSoundsDAO {
    @Query("SELECT * FROM classifiedSounds ORDER BY id DESC LIMIT 1")
    fun getLast(): ClassifiedSoundsTableModel

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertClassifiedSounds(classifiedSounds: ClassifiedSoundsTableModel)
}