package com.blub.openstores.repository

import com.blub.openstores.model.FoodTruck
import com.blub.openstores.repository.persistance.dao.FoodTruckDao
import com.blub.openstores.repository.service.FoodTruckService
import com.blub.openstores.utils.Constants.SF_GOV_APP_TOKEN
import javax.inject.Inject

open class FoodTruckRepository @Inject constructor(
    private val foodTruckService: FoodTruckService,
    private val foodTruckDao: FoodTruckDao
) {
    suspend open fun getFoodTrucks(forceUpdate: Boolean): List<FoodTruck> {
        if (forceUpdate) {
            val response = foodTruckService.getFoodTrucks(SF_GOV_APP_TOKEN, order = "applicant ASC")
            foodTruckDao.insertFoodTrucks(response)
        }

        return foodTruckDao.getFoodTrucks()
    }
}
