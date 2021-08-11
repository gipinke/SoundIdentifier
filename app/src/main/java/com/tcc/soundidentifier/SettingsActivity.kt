package com.tcc.soundidentifier

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.tcc.soundidentifier.constants.Settings
import com.tcc.soundidentifier.constants.UserData
import com.tcc.soundidentifier.database.dao.SoundVibrationTypeDAO
import com.tcc.soundidentifier.database.repository.ClassifiedSoundsRepository
import com.tcc.soundidentifier.database.repository.SoundVibrationTypeRepository
import com.tcc.soundidentifier.database.table.ClassifiedSoundsTableModel
import com.tcc.soundidentifier.database.table.SoundVibrationTypeTableModel


class SettingsActivity: AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private val TAG = "SettingsScreen"

    private lateinit var settingsTitle2: TextView


    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private lateinit var dogBarkSwitch: Switch
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private lateinit var carHornSwitch: Switch
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private lateinit var gunShotSwitch: Switch
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private lateinit var sirenSwitch: Switch

    private lateinit var dogBarkSpinnerText: TextView
    private lateinit var dogBarkSpinner: Spinner

    private lateinit var carHornSpinnerText: TextView
    private lateinit var carHornSpinner: Spinner

    private lateinit var gunShotSpinnerText: TextView
    private lateinit var gunShotSpinner: Spinner

    private lateinit var sirenSpinnerText: TextView
    private lateinit var sirenSpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Declare actionBar
        var actionBar = supportActionBar

        // Showing the back button in action bar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        // Declare text listeners
        settingsTitle2 = findViewById(R.id.settings_title2)

        // Declare switch listeners
        dogBarkSwitch = findViewById(R.id.dog_bark_switch)
        dogBarkSwitch.setOnCheckedChangeListener { _, isChecked ->
            Log.d(TAG, "Clicou em Latido de cachorro: $isChecked")
            UserData.dogBark = isChecked
            dogBarkSpinner.visibility = if (isChecked) View.VISIBLE else View.GONE
            dogBarkSpinnerText.visibility = if (isChecked) View.VISIBLE else View.GONE
            insertEnableSoundsInDatabase()
            checkVibrationTypeText()
        }

        carHornSwitch = findViewById(R.id.car_horn_switch)
        carHornSwitch.setOnCheckedChangeListener { _, isChecked ->
            Log.d(TAG, "Clicou em Buzina de Carro: $isChecked")
            UserData.carHorn = isChecked
            carHornSpinner.visibility = if (isChecked) View.VISIBLE else View.GONE
            carHornSpinnerText.visibility = if (isChecked) View.VISIBLE else View.GONE
            insertEnableSoundsInDatabase()
            checkVibrationTypeText()
        }

        gunShotSwitch = findViewById(R.id.gun_shot_switch)
        gunShotSwitch.setOnCheckedChangeListener { _, isChecked ->
            Log.d(TAG, "Clicou em Tiroteio: $isChecked")
            UserData.gunShot = isChecked
            gunShotSpinner.visibility = if (isChecked) View.VISIBLE else View.GONE
            gunShotSpinnerText.visibility = if (isChecked) View.VISIBLE else View.GONE
            insertEnableSoundsInDatabase()
            checkVibrationTypeText()
        }

        sirenSwitch = findViewById(R.id.siren_switch)
        sirenSwitch.setOnCheckedChangeListener { _, isChecked ->
            Log.d(TAG, "Clicou em Sirene: $isChecked")
            UserData.siren = isChecked
            sirenSpinner.visibility = if (isChecked) View.VISIBLE else View.GONE
            sirenSpinnerText.visibility = if (isChecked) View.VISIBLE else View.GONE
            insertEnableSoundsInDatabase()
            checkVibrationTypeText()
        }

        // Declare spinner listeners
        dogBarkSpinner = findViewById(R.id.dog_bark_spinner)
        dogBarkSpinnerText = findViewById(R.id.dog_bark_text_view2)
        dogBarkSpinner.onItemSelectedListener = this

        carHornSpinner = findViewById(R.id.car_horn_spinner)
        carHornSpinnerText = findViewById(R.id.car_horn_text_view2)
        carHornSpinner.onItemSelectedListener = this

        gunShotSpinner = findViewById(R.id.gun_shot_spinner)
        gunShotSpinnerText = findViewById(R.id.gun_shot_text_view2)
        gunShotSpinner.onItemSelectedListener = this

        sirenSpinner = findViewById(R.id.siren_spinner)
        sirenSpinnerText = findViewById(R.id.siren_text_view2)
        sirenSpinner.onItemSelectedListener = this

        ArrayAdapter.createFromResource(this,R.array.vibration_array,android.R.layout.simple_spinner_item)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                dogBarkSpinner.adapter = adapter
                carHornSpinner.adapter = adapter
                gunShotSpinner.adapter = adapter
                sirenSpinner.adapter = adapter
            }

        dogBarkSpinner.setSelection(UserData.dogBarkVibration)
        carHornSpinner.setSelection(UserData.carHornVibration)
        gunShotSpinner.setSelection(UserData.gunShotVibration)
        sirenSpinner.setSelection(UserData.sirenVibration)

        initSwitches()
    }

    override fun onItemSelected(arg0: AdapterView<*>, arg1: View, position: Int, id: Long) {
        when (arg0) {
            dogBarkSpinner -> {
                if (position == UserData.dogBarkVibration) return
                Log.d(TAG, "Clicou em Latido de cachorro")
                UserData.dogBarkVibration = position
            }
            carHornSpinner -> {
                if (position == UserData.carHornVibration) return
                Log.d(TAG, "Clicou em Buzina de Carro")
                UserData.carHornVibration = position
            }
            gunShotSpinner -> {
                if (position == UserData.gunShotVibration) return
                Log.d(TAG, "Clicou em Tiroteio")
                UserData.gunShotVibration = position
            }
            sirenSpinner -> {
                if (position == UserData.sirenVibration) return
                Log.d(TAG, "Clicou em Sirene")
                UserData.sirenVibration = position
            }
        }
        vibrate(position)
        insertSoundsVibrationTypeInDatabase()
    }

    override fun onNothingSelected(arg0: AdapterView<*>) {
    }

    private fun insertEnableSoundsInDatabase() {
        Log.d(TAG, "Inserindo no BD")
        val classifiedSound = ClassifiedSoundsTableModel(
            UserData.dogBark,
            UserData.gunShot,
            UserData.carHorn,
            UserData.siren
        )
        ClassifiedSoundsRepository.insertClassifiedSoundsLog(this, classifiedSound)
    }

    private fun insertSoundsVibrationTypeInDatabase() {
        Log.d(TAG, "Inserindo no BD")
        val soundVibrationType = SoundVibrationTypeTableModel(
            UserData.dogBarkVibration,
            UserData.gunShotVibration,
            UserData.carHornVibration,
            UserData.sirenVibration
        )
        SoundVibrationTypeRepository.insertSoundVibrationTypeLog(this, soundVibrationType)
    }

    private fun initSwitches() {
        dogBarkSwitch.isChecked = UserData.dogBark
        dogBarkSwitch.visibility = View.VISIBLE
        if (!dogBarkSwitch.isChecked) {
            dogBarkSpinner.visibility = View.GONE
            dogBarkSpinnerText.visibility = View.GONE
        }

        gunShotSwitch.isChecked = UserData.gunShot
        gunShotSwitch.visibility = View.VISIBLE
        if (!gunShotSwitch.isChecked) {
            gunShotSpinner.visibility = View.GONE
            gunShotSpinnerText.visibility = View.GONE
        }

        carHornSwitch.isChecked = UserData.carHorn
        carHornSwitch.visibility = View.VISIBLE
        if (!carHornSwitch.isChecked) {
            carHornSpinner.visibility = View.GONE
            carHornSpinnerText.visibility = View.GONE
        }

        sirenSwitch.isChecked = UserData.siren
        sirenSwitch.visibility = View.VISIBLE
        if (!sirenSwitch.isChecked) {
            sirenSpinner.visibility = View.GONE
            sirenSpinnerText.visibility = View.GONE
        }
        checkVibrationTypeText()
    }

    private fun checkVibrationTypeText() {
        if (!UserData.carHorn && !UserData.dogBark && !UserData.gunShot && !UserData.siren) {
            settingsTitle2.visibility = View.GONE
        } else {
            settingsTitle2.visibility = View.VISIBLE
        }
    }

    private fun vibrate(vibrationPatternInt: Int) {
        val vibrationConfig = this.getSystemService(VIBRATOR_SERVICE) as Vibrator
        val vibrationPattern = when(vibrationPatternInt) {
            Settings.defaultVibration -> Settings.defaultVibrationArray
            Settings.shortVibration -> Settings.shortVibrationArray
            Settings.longVibration -> Settings.longVibrationArray
            Settings.doubleVibration -> Settings.doubleVibrationArray
            Settings.tripleVibration -> Settings.tripleVibrationArray
            else -> Settings.defaultVibrationArray
        }

        vibrationConfig.vibrate(vibrationPattern, -1)
    }
}