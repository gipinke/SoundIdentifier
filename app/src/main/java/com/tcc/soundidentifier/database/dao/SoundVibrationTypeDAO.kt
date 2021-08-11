package com.tcc.soundidentifier.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tcc.soundidentifier.database.table.SoundVibrationTypeTableModel

@Dao
interface SoundVibrationTypeDAO {
    @Query("SELECT * FROM soundVibrationType ORDER BY id DESC LIMIT 1")
    fun getLast(): SoundVibrationTypeTableModel

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertClassifiedSounds(soundVibrationType: SoundVibrationTypeTableModel)
}