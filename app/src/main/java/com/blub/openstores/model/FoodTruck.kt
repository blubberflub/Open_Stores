package com.blub.openstores.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import com.squareup.moshi.Json

@Entity
data class FoodTruck(
    @Json(name = "addr_date_create")
    val addrDateCreate: String? = null,
    @Json(name = "addr_date_modified")
    val addrDateModified: String? = null,
    @Json(name = "applicant")
    val applicant: String? = null,
    @Json(name = "block")
    val block: String? = null,
    @Json(name = "cnn")
    val cnn: String? = null,
    @Json(name = "coldtruck")
    val coldtruck: String? = null,
    @Json(name = "dayofweekstr")
    val dayofweekstr: String? = null,
    @Json(name = "dayorder")
    val dayorder: String? = null,
    @Json(name = "end24")
    val end24: String? = null,
    @Json(name = "endtime")
    val endtime: String? = null,
    @Json(name = "latitude")
    val latitude: String? = null,
    @Json(name = "location")
    val location: String? = null,
    @Json(name = "location_2")
    val location2: Location2? = null,
    @Json(name = "locationdesc")
    val locationdesc: String? = null,
    @Json(name = "locationid")
    val locationid: String? = null,
    @Json(name = "longitude")
    val longitude: String? = null,
    @Json(name = "lot")
    val lot: String? = null,
    @Json(name = "optionaltext")
    val optionaltext: String? = null,
    @Json(name = "permit")
    val permit: String? = null,
    @Json(name = "start24")
    val start24: String? = null,
    @Json(name = "starttime")
    val starttime: String? = null,
    @Json(name = "x")
    val x: String? = null,
    @Json(name = "y")
    val y: String? = null
) {
    @PrimaryKey
    var id: String = "$locationid.$dayorder.$starttime-$endtime"

    fun getLatLong(): LatLng? {
        val lat = latitude?.toDouble()
        val long = longitude?.toDouble()

        if (lat == null || long == null) {
            return null
        }

        return LatLng(lat, long)
    }

    data class Location2(
        @Json(name = "coordinates")
        val coordinates: List<Double?>? = null,
        @Json(name = "type")
        val type: String? = null
    )
}