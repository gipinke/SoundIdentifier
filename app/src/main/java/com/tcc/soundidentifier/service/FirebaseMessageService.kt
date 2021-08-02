package com.tcc.soundidentifier.service

import android.annotation.SuppressLint
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.app.NotificationManager
import android.media.RingtoneManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.tcc.soundidentifier.MainActivity
import com.tcc.soundidentifier.R
import com.tcc.soundidentifier.constants.Settings
import com.tcc.soundidentifier.constants.UserData

class FirebaseMessageService : FirebaseMessagingService() {
    private val TAG = "FirebaseMessagingService"

    @SuppressLint("LongLogTag")
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.notification != null) {
            val audioClassified = remoteMessage.notification?.tag?.toInt()
            Log.d(TAG, "Tag áudio classificado: $audioClassified")
            showNotification(remoteMessage.notification?.title, remoteMessage.notification?.body)

//            if (audioClassified == Settings.carHorn && UserData.carHorn)
//                showNotification(remoteMessage.notification?.title, remoteMessage.notification?.body)
//            else if (audioClassified == Settings.dogBark && UserData.dogBark)
//                showNotification(remoteMessage.notification?.title, remoteMessage.notification?.body)
//            else if (audioClassified == Settings.gunShot && UserData.gunShot)
//                showNotification(remoteMessage.notification?.title, remoteMessage.notification?.body)
//            else if (audioClassified == Settings.siren && UserData.siren)
//                showNotification(remoteMessage.notification?.title, remoteMessage.notification?.body)
        }
    }

    private fun showNotification(title: String?, body: String?) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT)

        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setSound(soundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notificationBuilder.build())
    }
}