package com.blub.openstores.repository.persistance

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.blub.openstores.model.FoodTruck
import com.blub.openstores.repository.persistance.converters.LocationConverter
import com.blub.openstores.repository.persistance.dao.FoodTruckDao

@Database(entities = [FoodTruck::class], version = 1, exportSchema = true)
@TypeConverters(LocationConverter::class)
abstract class FoodTruckDatabase : RoomDatabase() {
    abstract fun foodTruckDao(): FoodTruckDao
}