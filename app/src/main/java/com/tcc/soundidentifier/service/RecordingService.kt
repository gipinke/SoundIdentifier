package com.tcc.soundidentifier.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.tcc.soundidentifier.RecorderActivity
import com.tcc.soundidentifier.constants.UserData
import java.io.DataOutputStream
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketTimeoutException

class RecordingService : Service() {
    private val TAG = "RecordingService"

    // Configs for socket server
    lateinit var streamThread: Thread
    lateinit var socket: Socket
    private val port = 5001
    private val connectionTimeout = 3000
    private val host = "192.168.0.12"

    // Configs for record audio
    private val sampleRate = 16000 // 44100 for music
    private val channelConfig = AudioFormat.CHANNEL_IN_MONO
    private val audioFormat = AudioFormat.ENCODING_PCM_16BIT
    var minBufSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat)
    var recorder: AudioRecord? = null

    // Boolean for service status
    var isServiceRunning = false

    override fun onCreate() {}

    override fun onStart(intent: Intent?, startId: Int) {
        streamThread = Thread {
            try {
                isServiceRunning = true
                // Start TCP socket connection
                socket = Socket()
                socket.connect(InetSocketAddress(host, port), connectionTimeout)
                Log.d(TAG, "Socket connected")

                socket.soTimeout = connectionTimeout

                // Send User Data to socket server
                socket.outputStream.write(UserData.firebaseToken?.toByteArray())
                socket.outputStream.write("${UserData.carHorn} ${UserData.gunShot} ${UserData.dogBark} ${UserData.siren}".toByteArray())

                val buffer = ByteArray(minBufSize)

                recorder = AudioRecord(
                    MediaRecorder.AudioSource.MIC,
                    sampleRate,
                    channelConfig,
                    audioFormat,
                    minBufSize * 10
                )

                recorder!!.startRecording()
                while (true) {
                    // Reading data from MIC into buffer
                    minBufSize = recorder!!.read(buffer, 0, buffer.size)

                    // Sending data through socket
                    val dOut = DataOutputStream(socket.getOutputStream())
                    dOut.write(buffer)
                }

            } catch (e: SocketTimeoutException) {
                Log.d(TAG, "SocketTimeoutException")
                e.printStackTrace()

            } catch (e: IllegalStateException) {
                Log.d(TAG, "IllegalStateException")
                e.printStackTrace()

            } catch (e: IOException) {
                Log.d(TAG, "IOException")
                e.printStackTrace()

            } finally {
                stopSelf()
                if (isServiceRunning) sendNotification()
            }
        }
        streamThread.start()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        Log.d(TAG, "Destroy service")
        isServiceRunning = false
        socket.close()
        streamThread.interrupt()
    }

    private fun sendNotification() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val text = applicationContext.getString(com.tcc.soundidentifier.R.string.timeout_error_text)
        val subtext = applicationContext.getString(com.tcc.soundidentifier.R.string.error_subtext)

        val intent = Intent(this, RecorderActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val builder = NotificationCompat.Builder(
            applicationContext,
            applicationContext.getString(com.tcc.soundidentifier.R.string.notification_channel_id)
        )

        builder
            .setSmallIcon(com.tcc.soundidentifier.R.mipmap.ic_launcher)
            .setContentTitle(text)
            .setContentText(subtext)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .priority = NotificationCompat.PRIORITY_HIGH

        notificationManager.notify(0, builder.build())
    }
}