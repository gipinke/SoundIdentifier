package com.tcc.soundidentifier.service

import android.annotation.SuppressLint
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.app.NotificationManager
import android.media.RingtoneManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.core.app.NotificationCompat
import com.tcc.soundidentifier.MainActivity
import com.tcc.soundidentifier.R
import com.tcc.soundidentifier.constants.Settings
import com.tcc.soundidentifier.constants.UserData

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class FirebaseMessageService : FirebaseMessagingService() {
    private val TAG = "FirebaseMessagingService"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.notification != null) {
            val audioClassified = remoteMessage.notification?.tag?.toInt()
            Log.d(TAG, "Tag áudio classificado: $audioClassified")

            val vibrationPatternInt = when(audioClassified) {
                Settings.dogBark -> UserData.dogBarkVibration
                Settings.gunShot -> UserData.gunShotVibration
                Settings.carHorn -> UserData.carHornVibration
                Settings.siren -> UserData.sirenVibration
                else -> -1
            }
            if (vibrationPatternInt >= 0) vibrate(vibrationPatternInt)
        }
    }

    private fun vibrate(vibrationPatternInt: Int) {
        val vibrationConfig = this.getSystemService(VIBRATOR_SERVICE) as Vibrator
        val vibrationPattern = when(vibrationPatternInt) {
            Settings.defaultVibration -> Settings.defaultVibrationArray
            Settings.shortVibration -> Settings.shortVibrationArray
            Settings.longVibration -> Settings.longVibrationArray
            Settings.doubleVibration -> Settings.doubleVibrationArray
            Settings.tripleVibration -> Settings.tripleVibrationArray
            else -> Settings.defaultVibrationArray
        }

        vibrationConfig.vibrate(vibrationPattern, -1)
    }
}