package com.tcc.soundidentifier.constants

object Settings {
    // For enable sounds
    const val carHorn = 0
    const val dogBark = 1
    const val gunShot = 2
    const val siren = 3

    // Types of vibration
    const val defaultVibration = 0
    const val shortVibration = 1
    const val longVibration = 2
    const val doubleVibration = 3
    const val tripleVibration = 4

    val defaultVibrationArray = longArrayOf(0, 100)
    val shortVibrationArray = longArrayOf(0, 50)
    val longVibrationArray = longArrayOf(0, 200)
    val doubleVibrationArray = longArrayOf(0, 100, 50, 100)
    val tripleVibrationArray = longArrayOf(0, 100, 50, 100, 50, 100)
}