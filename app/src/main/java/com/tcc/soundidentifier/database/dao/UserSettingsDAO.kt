package com.tcc.soundidentifier.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tcc.soundidentifier.database.table.UserSettingsTableModel

@Dao
interface UserSettingsDAO {
    @Query("SELECT * FROM userSettings ORDER BY id DESC LIMIT 1")
    fun getLast(): UserSettingsTableModel

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUserSettings(userSettings: UserSettingsTableModel)
}