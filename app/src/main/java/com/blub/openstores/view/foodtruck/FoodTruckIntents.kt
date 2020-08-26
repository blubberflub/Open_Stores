package com.blub.openstores.view.foodtruck

import com.blub.openstores.model.FoodTruck

sealed class Intent

object InitIntent : Intent()

object StartIntent : Intent()

object RefreshIntent : Intent()

object FragmentToggleIntent : Intent()

data class MarkerSelectedIntent(val foodTruck: FoodTruck) : Intent()

object NonMarkerSelectedIntent : Intent()