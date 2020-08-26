package com.blub.openstores.repository.persistance.dao

import androidx.room.*
import com.blub.openstores.model.FoodTruck

@Dao
interface FoodTruckDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoodTrucks(foodTruckList: List<FoodTruck>)

    @Query("SELECT * FROM foodtruck")
    suspend fun getFoodTrucks(): List<FoodTruck>
}
