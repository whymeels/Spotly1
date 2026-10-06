package com.example.spotly.data.dao

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Query
import com.example.spotly.data.entity.CityEntity
import com.example.spotly.data.entity.PlaceEntity

data class PlaceWithCategory(
    @Embedded
    val place: PlaceEntity,

    val categoryName: String
)

@Dao
interface PlaceDao {

    @Query("SELECT * FROM City ORDER BY CityID")
    suspend fun getCities(): List<CityEntity>

    @Query(
        """
        SELECT Place.*, Category.Name AS categoryName
        FROM Place
        INNER JOIN Category
            ON Place.CategoryID = Category.CategoryID
        ORDER BY Place.Name
        """
    )
    suspend fun getPlaces(): List<PlaceWithCategory>
}