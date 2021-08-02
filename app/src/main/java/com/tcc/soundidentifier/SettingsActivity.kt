package com.tcc.soundidentifier

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import com.tcc.soundidentifier.constants.UserData
import com.tcc.soundidentifier.database.repository.ClassifiedSoundsRepository
import com.tcc.soundidentifier.database.table.ClassifiedSoundsTableModel


class SettingsActivity: AppCompatActivity() {
    private val TAG = "SettingsScreen"

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private lateinit var dogBarkSwitch: Switch
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private lateinit var carHornSwitch: Switch
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private lateinit var gunShotSwitch: Switch
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private lateinit var sirenSwitch: Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Declare actionBar
        var actionBar = supportActionBar

        // Showing the back button in action bar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        // Declare switch listeners
        dogBarkSwitch = findViewById(R.id.dog_bark_switch)
        dogBarkSwitch.setOnCheckedChangeListener { _, isChecked ->
            Log.d(TAG, "Clicou em Latido de cachorro: $isChecked")
            UserData.dogBark = isChecked
            insertInDatabase()
        }

        carHornSwitch = findViewById(R.id.car_horn_switch)
        carHornSwitch.setOnCheckedChangeListener { _, isChecked ->
            Log.d(TAG, "Clicou em Buzina de Carro: $isChecked")
            UserData.carHorn = isChecked
            insertInDatabase()
        }

        gunShotSwitch = findViewById(R.id.gun_shot_switch)
        gunShotSwitch.setOnCheckedChangeListener { _, isChecked ->
            Log.d(TAG, "Clicou em Tiroteio: $isChecked")
            UserData.gunShot = isChecked
            insertInDatabase()
        }

        sirenSwitch = findViewById(R.id.siren_switch)
        sirenSwitch.setOnCheckedChangeListener { _, isChecked ->
            Log.d(TAG, "Clicou em Sirene: $isChecked")
            UserData.siren = isChecked
            insertInDatabase()
        }
        initSwitches()
    }

    private fun insertInDatabase() {
        Log.d(TAG, "Inserindo no BD")
        val classifiedSound = ClassifiedSoundsTableModel(
            UserData.dogBark,
            UserData.gunShot,
            UserData.carHorn,
            UserData.siren
        )
        ClassifiedSoundsRepository.insertClassifiedSoundsLog(this, classifiedSound)
    }

    private fun initSwitches() {
        dogBarkSwitch.isChecked = UserData.dogBark
        dogBarkSwitch.visibility = View.VISIBLE

        gunShotSwitch.isChecked = UserData.gunShot
        gunShotSwitch.visibility = View.VISIBLE

        carHornSwitch.isChecked = UserData.carHorn
        carHornSwitch.visibility = View.VISIBLE

        sirenSwitch.isChecked = UserData.siren
        sirenSwitch.visibility = View.VISIBLE
    }
}