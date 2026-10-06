package com.example.spotly.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "Intent",
    indices = [
        Index(value = ["Name"], unique = true)
    ]
)
data class IntentEntity(
    @PrimaryKey
    @ColumnInfo(name = "IntentID")
    val intentId: Int,

    @ColumnInfo(name = "Name")
    val name: String
)