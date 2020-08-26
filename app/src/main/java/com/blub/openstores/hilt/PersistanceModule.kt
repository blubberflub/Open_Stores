package com.blub.openstores.hilt

import android.app.Application
import androidx.room.Room
import com.blub.openstores.repository.persistance.FoodTruckDatabase
import com.blub.openstores.repository.persistance.dao.FoodTruckDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object PersistanceModule {
    @Singleton
    @Provides
    fun providesFoodTruckDatabase(application: Application): FoodTruckDatabase {
        return Room
            .databaseBuilder(application, FoodTruckDatabase::class.java, "Foodtruck.db")
            .build()
    }

    @Singleton
    @Provides
    fun providesFoodTruckDao(foodTruckDatabase: FoodTruckDatabase): FoodTruckDao {
        return foodTruckDatabase.foodTruckDao()
    }
}