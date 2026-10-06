package com.example.spotly.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "PlacePhoto",
    foreignKeys = [
        ForeignKey(
            entity = PlaceEntity::class,
            parentColumns = ["PlaceID"],
            childColumns = ["PlaceID"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.NO_ACTION
        )
    ]
)
data class PlacePhotoEntity(
    @PrimaryKey
    @ColumnInfo(name = "PhotoID")
    val photoId: Int,

    @ColumnInfo(name = "PlaceID")
    val placeId: Int,

    @ColumnInfo(name = "ImagePath")
    val imagePath: String,

    @ColumnInfo(name = "Position")
    val position: Int,

    @ColumnInfo(name = "Author")
    val author: String,

    @ColumnInfo(name = "SourceUrl")
    val sourceUrl: String?,

    @ColumnInfo(name = "License")
    val license: String
)