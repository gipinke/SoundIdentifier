package com.tcc.soundidentifier

import android.app.ActivityManager
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.tcc.soundidentifier.service.RecordingService
import kotlin.system.exitProcess

class RecorderActivity: AppCompatActivity() {
    private val TAG = "RecorderScreen"
    private var screenChange = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recorder)

        // Hide actionBar
        val actionbar: ActionBar? = supportActionBar
        actionbar!!.hide()

        // Start Service
        if (!isMyServiceRunning(RecordingService::class.java)) {
            startService()
        }

        // Declare buttons listeners
        val settingButton = findViewById<ImageButton>(R.id.setting_button)
        settingButton.setOnClickListener {
            onSettingPressed()
        }
    }

    override fun onBackPressed() {
        // do nothing
    }

    private fun onSettingPressed() {
        Log.d(TAG, "Redirecionar para tela de configurações")
        screenChange = true
        stopService(Intent(this, RecordingService::class.java))

        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    private fun startService() {
        startService(Intent(this, RecordingService::class.java))
        bindService(
            Intent(this, RecordingService::class.java),
            object : ServiceConnection {
                override fun onServiceConnected(name: ComponentName?, service: IBinder?) {}
                override fun onServiceDisconnected(componentName: ComponentName) {}
                override fun onBindingDied(name: ComponentName?) {
                    super.onBindingDied(name)
                    if (!screenChange) alertMessage()
                }
            },
            0
        )
    }

    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

    private fun alertMessage() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val text = this.getString(R.string.timeout_error_text)
        val subtext = this.getString(R.string.error_subtext)
        val alertButtonText = this.getString(R.string.alert_button_text)

        try {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(text)
            builder.setMessage(subtext)
            builder.setPositiveButton(alertButtonText){ _dialog, _which ->
                Log.d(TAG, "Fechando app")
                this.finishAffinity()
                notificationManager.cancelAll()
                exitProcess(0)
            }
            builder.setCancelable(false)
            val dialog: AlertDialog = builder.create()
            dialog.show()
        } catch (e: Exception) {
            Log.d(TAG, "Erro ao mostrar o alert")
        }
    }
}