package com.tcc.soundidentifier.database.repository

import android.content.Context
import android.util.Log
import com.tcc.soundidentifier.database.ClassifiedSoundsDatabase
import com.tcc.soundidentifier.database.table.ClassifiedSoundsTableModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class ClassifiedSoundsRepository {
    companion object{
        val TAG = "ClassifiedSoundsRepository"

        private var classifiedSoundsDatabase: ClassifiedSoundsDatabase? = null

        private fun initializeDB(context: Context) : ClassifiedSoundsDatabase {
            return ClassifiedSoundsDatabase.getDatabase(context)
        }

        fun insertClassifiedSoundsLog(context: Context, classifiedSounds: ClassifiedSoundsTableModel) {
            classifiedSoundsDatabase = initializeDB(context)
            CoroutineScope(IO).launch {
                classifiedSoundsDatabase!!.classifiedSoundsDao().insertClassifiedSounds(classifiedSounds)
                Log.d(TAG, "Insert value with success")
            }
        }

        fun getClassifiedSoundsLastRow(context: Context): ClassifiedSoundsTableModel {
            classifiedSoundsDatabase = initializeDB(context)
            val lastRow = classifiedSoundsDatabase!!.classifiedSoundsDao().getLast()
            Log.d(TAG, "Insert value with success")

            return lastRow
        }
    }
}