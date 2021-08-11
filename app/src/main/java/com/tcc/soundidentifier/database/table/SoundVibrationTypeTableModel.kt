package com.tcc.soundidentifier.database.table

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "soundVibrationType")
data class SoundVibrationTypeTableModel(
    @ColumnInfo(name = "dog_bark_vibration")
    val dog_bark_vibration: Int,
    @ColumnInfo(name = "gun_shot_vibration")
    val gun_shot_vibration: Int,
    @ColumnInfo(name = "car_horn_vibration")
    val car_horn_vibration: Int,
    @ColumnInfo(name = "siren_vibration")
    val siren_vibration: Int
) {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}