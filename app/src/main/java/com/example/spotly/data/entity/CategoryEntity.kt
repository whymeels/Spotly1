package com.example.spotly.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "Category",
    indices = [
        Index(value = ["Name"], unique = true)
    ]
)
data class CategoryEntity(
    @PrimaryKey
    @ColumnInfo(name = "CategoryID")
    val categoryId: Int,

    @ColumnInfo(name = "Name")
    val name: String
)