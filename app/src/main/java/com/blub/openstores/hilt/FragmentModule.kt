package com.blub.openstores.hilt

import com.blub.openstores.view.foodtruck.list.FoodTruckListFragment
import com.blub.openstores.view.foodtruck.map.FoodTruckMapFragment
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ActivityComponent::class)
object FragmentModule {

    @Provides
    @Reusable
    fun providesFoodTruckListFragment() = FoodTruckListFragment()

    @Provides
    @Reusable
    fun providesFoodTruckMapFragment() = FoodTruckMapFragment()
}