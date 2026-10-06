package com.example.spotly.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "City")
data class CityEntity(
    @PrimaryKey
    @ColumnInfo(name = "CityID")
    val cityId: Int,

    @ColumnInfo(name = "Name")
    val name: String,

    @ColumnInfo(name = "Latitude")
    val latitude: Double,

    @ColumnInfo(name = "Longitude")
    val longitude: Double
)