package com.tcc.soundidentifier.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tcc.soundidentifier.database.dao.ClassificationSoundsDao
import com.tcc.soundidentifier.database.table.ClassifiedSoundsTableModel

@Database(entities = [ClassifiedSoundsTableModel::class], version = 1, exportSchema = true)
abstract class ClassifiedSoundsDatabase : RoomDatabase() {
    abstract fun classifiedSoundsDao(): ClassificationSoundsDao

    companion object {
        // Singleton prevents multiple instances of database opening at the same time.
        @Volatile
        private var INSTANCE: ClassifiedSoundsDatabase? = null

        fun getDatabase(context: Context): ClassifiedSoundsDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ClassifiedSoundsDatabase::class.java,
                    "word_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}
