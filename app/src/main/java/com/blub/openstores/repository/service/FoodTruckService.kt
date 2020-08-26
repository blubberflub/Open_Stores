package com.blub.openstores.repository.service

import com.blub.openstores.model.FoodTruck
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface FoodTruckService {

    @GET("resource/jjew-r69b.json")
    suspend fun getFoodTrucks(@Header("X-App-Token") token: String,
                              @Query("\$limit") limit: Int = 5000,
                              @Query("\$order") order: String): List<FoodTruck>

}
