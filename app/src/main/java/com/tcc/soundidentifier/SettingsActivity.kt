package com.tcc.soundidentifier

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity


class SettingsActivity: AppCompatActivity() {
    private val TAG = "SettingsScreen"

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Declare actionBar
        var actionBar = supportActionBar

        // Showing the back button in action bar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        // Declare switch listeners
        val dogBarkSwitch = findViewById<Switch>(R.id.dog_bark_switch)
        dogBarkSwitch.setOnCheckedChangeListener { _, isChecked ->
            Log.d(TAG, "Clicou em Latido de cachorro: $isChecked")
        }

        val carHornSwitch = findViewById<Switch>(R.id.car_horn_switch)
        carHornSwitch.setOnCheckedChangeListener { _, isChecked ->
            Log.d(TAG, "Clicou em Buzina de Carro: $isChecked")
        }

        val gunShotSwitch = findViewById<Switch>(R.id.gun_shot_switch)
        gunShotSwitch.setOnCheckedChangeListener { _, isChecked ->
            Log.d(TAG, "Clicou em Tiroteio: $isChecked")
        }

        val sirenSwitch = findViewById<Switch>(R.id.siren_switch)
        sirenSwitch.setOnCheckedChangeListener { _, isChecked ->
            Log.d(TAG, "Clicou em Sirene: $isChecked")
        }
    }
}