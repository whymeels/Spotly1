package com.example.spotly.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "PlaceIntent",
    primaryKeys = ["PlaceID", "IntentID"],
    foreignKeys = [
        ForeignKey(
            entity = PlaceEntity::class,
            parentColumns = ["PlaceID"],
            childColumns = ["PlaceID"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.NO_ACTION
        ),
        ForeignKey(
            entity = IntentEntity::class,
            parentColumns = ["IntentID"],
            childColumns = ["IntentID"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.NO_ACTION
        )
    ]
)
data class PlaceIntentEntity(
    @ColumnInfo(name = "PlaceID")
    val placeId: Int,

    @ColumnInfo(name = "IntentID")
    val intentId: Int
)