package com.example.finalproject

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
import androidx.appcompat.app.AppCompatActivity
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class RecorderActivity: AppCompatActivity() {
    private val TAG = "RecorderScreen"
    private var recordingState = false

    private val port = 50005
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

        startRecording()

        // Declare buttons listeners
        val settingButton = findViewById<ImageButton>(R.id.setting_button)
        settingButton.setOnClickListener {
            onSettingPressed(vibrationConfig)
        }
    }

    override fun onBackPressed() {
        // do nothing
    }

    private fun onSettingPressed(vibrationConfig: Vibrator) {
        Log.d(TAG, "Redirecionar para tela de configurações")
        vibrationConfig.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    private fun startRecording() {
        val streamThread = Thread {
            try {
                val socket = DatagramSocket()
                Log.d(TAG, "Socket Created")

                val buffer = ByteArray(minBufSize)

                Log.d(TAG, "Buffer created of size $minBufSize")
                var packet: DatagramPacket

                val destination = InetAddress.getByName("192.168.0.16");
                Log.d(TAG, "Address retrieved");

                recorder = AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate, channelConfig, audioFormat, minBufSize * 10)
                Log.d(TAG, "Recorder initialized")

                recorder!!.startRecording()

                recordingState = true

                while (recordingState) {
                    //reading data from MIC into buffer
                    minBufSize = recorder!!.read(buffer, 0, buffer.size)

                    //putting buffer in the packet
                    packet = DatagramPacket(buffer, buffer.size, destination, port)
                    socket.send(packet)
                }

            } catch (e: IllegalStateException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        streamThread.start()
    }
}