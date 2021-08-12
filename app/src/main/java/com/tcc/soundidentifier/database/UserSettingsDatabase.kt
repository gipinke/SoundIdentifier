package com.tcc.soundidentifier.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tcc.soundidentifier.database.dao.UserSettingsDAO
import com.tcc.soundidentifier.database.table.UserSettingsTableModel

@Database(entities = [UserSettingsTableModel::class], version = 1, exportSchema = true)
abstract class UserSettingsDatabase : RoomDatabase() {
    abstract fun userSettingsDAO(): UserSettingsDAO

    companion object {
        // Singleton prevents multiple instances of database opening at the same time.
        @Volatile
        private var INSTANCE: UserSettingsDatabase? = null

        fun getDatabase(context: Context): UserSettingsDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserSettingsDatabase::class.java,
                    "word_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}
