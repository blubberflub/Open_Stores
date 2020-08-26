package com.blub.openstores.repository.persistance.converters

import androidx.room.TypeConverter
import com.blub.openstores.model.FoodTruck
import com.blub.openstores.utils.MoshiUtils
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class LocationConverter {

    @TypeConverter
    fun fromLocation2ToCoordinates(location2: FoodTruck.Location2?): String? {
        return location2?.let {
            return MoshiUtils.toJson(location2)
        }
    }

    @TypeConverter
    fun fromCoordinatesToLocation2(json: String?): FoodTruck.Location2? {
        return json?.let {
            return MoshiUtils.fromJson(json)
        }
    }
}
