package com.tcc.soundidentifier.database.repository

import android.content.Context
import android.util.Log
import com.tcc.soundidentifier.database.UserSettingsDatabase
import com.tcc.soundidentifier.database.table.UserSettingsTableModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class UserSettingsRepository {
    companion object{
        val TAG = "UserSettingsRepository"

        private var userSettingsDatabase: UserSettingsDatabase? = null

        private fun initializeDB(context: Context) : UserSettingsDatabase {
            return UserSettingsDatabase.getDatabase(context)
        }

        fun insertClassifiedSoundsLog(context: Context, userSettings: UserSettingsTableModel) {
            userSettingsDatabase = initializeDB(context)
            CoroutineScope(IO).launch {
                userSettingsDatabase!!.userSettingsDAO().insertUserSettings(userSettings)
                Log.d(TAG, "Insert value with success")
            }
        }

        fun getClassifiedSoundsLastRow(context: Context): UserSettingsTableModel {
            userSettingsDatabase = initializeDB(context)
            val lastRow = userSettingsDatabase!!.userSettingsDAO().getLast()
            Log.d(TAG, "Insert value with success")

            return lastRow
        }
    }
}