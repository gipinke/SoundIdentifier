package com.tcc.soundidentifier

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
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.tcc.soundidentifier.constants.UserData
import com.tcc.soundidentifier.database.repository.ClassifiedSoundsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val permissionRequestCode = 0
    private val TAG = "PermissionsScreen"
    private var permissionsCalled = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_main)

        // Init values of classifiedSounds
        CoroutineScope(Dispatchers.IO).launch { initializeClassifiedSoundsValues() }

        // Start Firebase Messaging
        startFirebaseMessaging()

        // Hide actionBar
        val actionbar: ActionBar? = supportActionBar
        actionbar!!.hide()

        // Check if user has permission permissions
        checkPermissions()

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
                Log.d(TAG, "Permissões aceitas, redirecionar para tela de gravação de áudio")
                redirectToRecorderActivity()
            } else {
                Log.d(TAG, "Permissões negadas")
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
        Log.d(TAG, "Trocando de Tela")
        val intent = Intent(this, RecorderActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun redirectToAppPermissionsScreen() {
        Log.d(TAG, "Vá aos ajustes do aplicativo para aceitar as permissões")
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }

    private fun checkPermissions() {
        val recordAudioPermissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
        val writeExternalStoragePermissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
        Log.d(TAG, "Verificar se usuário tem as permissões aceitas")

        if (recordAudioPermissionStatus == PackageManager.PERMISSION_GRANTED &&
                writeExternalStoragePermissionStatus == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Permissões aceitas, redirecionar para tela de gravação de áudio")
            redirectToRecorderActivity()
        }
    }

    private fun initializeClassifiedSoundsValues() {
        Log.d(TAG, "Recebendo dados do DB")
        val lastRow = ClassifiedSoundsRepository.getClassifiedSoundsLastRow(this)
        if (lastRow != null) {
            UserData.dogBark = lastRow.dog_bark
            UserData.gunShot = lastRow.gun_shot
            UserData.carHorn = lastRow.car_horn
            UserData.siren = lastRow.siren
        }
    }

    private fun startFirebaseMessaging() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.d(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            UserData.firebaseToken = task.result

            // Log and toast
            Log.d(TAG, "Token: ${UserData.firebaseToken}")
        })
    }
}