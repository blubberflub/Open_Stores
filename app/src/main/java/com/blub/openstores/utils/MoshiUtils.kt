package com.blub.openstores.utils

import com.blub.openstores.model.FoodTruck
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

object MoshiUtils {
    val moshi = Moshi
        .Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    inline fun <reified T> toJson(any: T?): String? {
        val adapter = moshi.adapter(T::class.java)

        return adapter.toJson(any)
    }

    inline fun <reified T> fromJson(json: String): T? {
        val adapter = moshi.adapter(T::class.java)

        return adapter.fromJson(json)
    }
}