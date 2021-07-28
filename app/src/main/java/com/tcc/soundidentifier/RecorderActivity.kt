package com.tcc.soundidentifier

import android.content.Intent
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import java.io.*
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketTimeoutException
import java.util.*


class RecorderActivity: AppCompatActivity() {
    private val TAG = "RecorderScreen"

    // Configs for socket server
    lateinit var streamThread: Thread
    lateinit var socket: Socket
    private val port = 5001
    private val connectionTimeout = 3000
    private val host = "192.168.0.12"
    private var socketStarted = false

    // Configs for record audio
    private val sampleRate = 16000 // 44100 for music
    private val channelConfig = AudioFormat.CHANNEL_IN_MONO
    private val audioFormat = AudioFormat.ENCODING_PCM_16BIT
    var minBufSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat)
    var recorder: AudioRecord? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recorder)

        // Hide actionBar
        val actionbar: ActionBar? = supportActionBar
        actionbar!!.hide()

        // Declare variables
        val vibrationConfig = this.getSystemService(VIBRATOR_SERVICE) as Vibrator

        // Start Socket Connection
        if (!socketStarted) {
            startSocketConnection()
            socketStarted = true
        }

        // Declare buttons listeners
        val settingButton = findViewById<ImageButton>(R.id.setting_button)
        settingButton.setOnClickListener {
            onSettingPressed(vibrationConfig)
        }

        // Start Firebase Messaging
        startFirebaseMessaging()
    }

    override fun onBackPressed() {
        // do nothing
    }

    private fun startFirebaseMessaging() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.d(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            Log.d(TAG, "Token: $token")
        })
    }

    private fun onSettingPressed(vibrationConfig: Vibrator) {
        Log.d(TAG, "Redirecionar para tela de configurações")
        vibrationConfig.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    private fun startSocketConnection() {
        streamThread = Thread {
            try {
                // Start TCP socket connection
                socket = Socket()
                socket.connect(InetSocketAddress(host, port), connectionTimeout)
                Log.d(TAG, "Socket connected")

                socket.soTimeout = connectionTimeout

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
                this.runOnUiThread { alertMessage() }
                if (::socket.isInitialized) {
                    socket.close()
                }
            } catch (e: IllegalStateException) {
                Log.d(TAG, "IllegalStateException")
                e.printStackTrace()
                this.runOnUiThread { alertMessage() }
                if (::socket.isInitialized) {
                    socket.close()
                }
            } catch (e: IOException) {
                Log.d(TAG, "IOException")
                e.printStackTrace()
                this.runOnUiThread { alertMessage() }
                if (::socket.isInitialized) {
                    socket.close()
                }
            }
        }
        streamThread.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::socket.isInitialized) {
            socket.close()
        }
        if (::streamThread.isInitialized) {
            streamThread.interrupt()
        }
    }

    private fun alertMessage() {
        val text = this.getString(R.string.timeout_error_text)
        val subtext = this.getString(R.string.error_subtext)
        val alertButtonText = this.getString(R.string.alert_button_text)

        val builder = AlertDialog.Builder(this)
        builder.setTitle(text)
        builder.setMessage(subtext)
        builder.setPositiveButton(alertButtonText){ _dialog, _which ->
            Log.d(TAG, "Fechando app")
            this.finishAffinity()
        }
        builder.setCancelable(false)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}