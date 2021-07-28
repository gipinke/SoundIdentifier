package com.tcc.soundidentifier.database.table

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "classifiedSounds")
data class ClassifiedSoundsTableModel(
    @ColumnInfo(name = "dog_bark")
    val dog_bark: Boolean,
    @ColumnInfo(name = "gun_shot")
    val gun_shot: Boolean,
    @ColumnInfo(name = "car_horn")
    val car_horn: Boolean,
    @ColumnInfo(name = "siren")
    val siren: Boolean
) {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}