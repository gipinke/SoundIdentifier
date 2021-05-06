package com.example.finalproject

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.Settings
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    private val permissionRequestCode = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_main)

        // Hide actionBar
        val actionbar: ActionBar? = supportActionBar
        actionbar!!.hide()

        // Declare variables
        var permissionsCalled = 0

        // Check Permissions
        val recordAudioPermissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
        val writeExternalStoragePermissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)

        if (recordAudioPermissionStatus == PackageManager.PERMISSION_GRANTED &&
            writeExternalStoragePermissionStatus == PackageManager.PERMISSION_GRANTED) {
            Log.d("FinalProjectDebugLog", "Permissões aceitas, redirecionar para tela de gravação de áudio")
            redirectToRecorderActivity()
        }

        // Declare buttons listeners
        val acceptButton = findViewById<Button>(R.id.accept_button)
        acceptButton.setOnClickListener {
            val vibrationConfig = this.getSystemService(VIBRATOR_SERVICE) as Vibrator
            vibrationConfig.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))

            if (permissionsCalled < 2) {
                requestPermissions()
                permissionsCalled += 1
            } else redirectToAppPermissionsScreen()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == permissionRequestCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("FinalProjectDebugLog", "Permissões aceitas, redirecionar para tela de gravação de áudio")
                redirectToRecorderActivity()
            } else {
                Log.d("FinalProjectDebugLog", "Permissões negadas")
            }
        }
    }

    private fun requestPermissions() {
        val permissions = arrayOf(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE)

        requestPermissions(permissions, permissionRequestCode)
    }

    private fun redirectToRecorderActivity() {
        val intent = Intent(this, RecorderActivity::class.java)
        startActivity(intent)
    }

    private fun redirectToAppPermissionsScreen() {
        Log.d("FinalProjectDebugLog", "Vá aos ajustes do aplicativo para aceitar as permissões")
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }
}