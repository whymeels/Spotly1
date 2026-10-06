package com.example.spotly.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "Place",
    foreignKeys = [
        ForeignKey(
            entity = CityEntity::class,
            parentColumns = ["CityID"],
            childColumns = ["CityID"],
            onDelete = ForeignKey.NO_ACTION,
            onUpdate = ForeignKey.NO_ACTION
        ),
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["CategoryID"],
            childColumns = ["CategoryID"],
            onDelete = ForeignKey.NO_ACTION,
            onUpdate = ForeignKey.NO_ACTION
        )
    ]
)
data class PlaceEntity(
    @PrimaryKey
    @ColumnInfo(name = "PlaceID")
    val placeId: Int,

    @ColumnInfo(name = "CityID")
    val cityId: Int,

    @ColumnInfo(name = "CategoryID")
    val categoryId: Int,

    @ColumnInfo(name = "Name")
    val name: String,

    @ColumnInfo(name = "Description")
    val description: String,

    @ColumnInfo(name = "Address")
    val address: String,

    @ColumnInfo(name = "Latitude")
    val latitude: Double,

    @ColumnInfo(name = "Longitude")
    val longitude: Double,

    @ColumnInfo(name = "VisitDurationMinutes")
    val visitDurationMinutes: Int?,

    @ColumnInfo(name = "BestVisitTime")
    val bestVisitTime: String?,

    @ColumnInfo(name = "BestPhotoTime")
    val bestPhotoTime: String?,

    @ColumnInfo(name = "PhotoTips")
    val photoTips: String?,

    @ColumnInfo(name = "IsFreeEntry")
    val isFreeEntry: Boolean?,

    @ColumnInfo(name = "PriceNote")
    val priceNote: String?,

    @ColumnInfo(name = "AverageCheckRub")
    val averageCheckRub: Int?,

    @ColumnInfo(name = "SourceUrl")
    val sourceUrl: String?,

    @ColumnInfo(name = "VerifiedAt")
    val verifiedAt: String?
)