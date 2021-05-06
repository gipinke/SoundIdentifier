package com.example.finalproject

import android.content.Intent
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity


class RecorderActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recorder)

        // Hide actionBar
        val actionbar: ActionBar? = supportActionBar
        actionbar!!.hide()

        // Declare variables
        val vibrationConfig = this.getSystemService(VIBRATOR_SERVICE) as Vibrator

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
        Log.d("FinalProjectDebugLog", "Redirecionar para tela de configurações")
        vibrationConfig.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }
}