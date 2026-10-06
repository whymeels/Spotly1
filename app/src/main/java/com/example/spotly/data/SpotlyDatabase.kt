package com.example.spotly.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.spotly.data.dao.PlaceDao
import com.example.spotly.data.entity.CategoryEntity
import com.example.spotly.data.entity.CityEntity
import com.example.spotly.data.entity.IntentEntity
import com.example.spotly.data.entity.PlaceEntity
import com.example.spotly.data.entity.PlaceIntentEntity
import com.example.spotly.data.entity.PlacePhotoEntity

@Database(
    entities = [
        CityEntity::class,
        CategoryEntity::class,
        IntentEntity::class,
        PlaceEntity::class,
        PlacePhotoEntity::class,
        PlaceIntentEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class SpotlyDatabase : RoomDatabase() {

    abstract fun placeDao(): PlaceDao

    companion object {

        @Volatile
        private var INSTANCE: SpotlyDatabase? = null

        fun getInstance(context: Context): SpotlyDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    SpotlyDatabase::class.java,
                    "spotly.db"
                )
                    .createFromAsset("databases/spotly.db")
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}