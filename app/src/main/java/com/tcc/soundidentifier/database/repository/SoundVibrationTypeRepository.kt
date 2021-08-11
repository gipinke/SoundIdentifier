package com.tcc.soundidentifier.database.repository

import android.content.Context
import android.util.Log
import com.tcc.soundidentifier.database.ClassifiedSoundsDatabase
import com.tcc.soundidentifier.database.table.ClassifiedSoundsTableModel
import com.tcc.soundidentifier.database.table.SoundVibrationTypeTableModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class SoundVibrationTypeRepository {
    companion object{
        val TAG = "SoundVibrationTypeRepository"

        private var soundVibrationTypeDatabase: ClassifiedSoundsDatabase? = null

        private fun initializeDB(context: Context) : ClassifiedSoundsDatabase {
            return ClassifiedSoundsDatabase.getDatabase(context)
        }

        fun insertSoundVibrationTypeLog(context: Context, soundVibrationType: SoundVibrationTypeTableModel) {
            soundVibrationTypeDatabase = initializeDB(context)
            CoroutineScope(IO).launch {
                soundVibrationTypeDatabase!!.soundVibrationTypeDao().insertClassifiedSounds(soundVibrationType)
                Log.d(TAG, "Insert value with success")
            }
        }

        fun getSoundVibrationTypeLastRow(context: Context): SoundVibrationTypeTableModel {
            soundVibrationTypeDatabase = initializeDB(context)
            val lastRow = soundVibrationTypeDatabase!!.soundVibrationTypeDao().getLast()
            Log.d(TAG, "Insert value with success")

            return lastRow
        }
    }
}