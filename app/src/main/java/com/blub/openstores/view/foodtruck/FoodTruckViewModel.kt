package com.blub.openstores.view.foodtruck

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blub.openstores.model.FoodTruck
import com.blub.openstores.repository.FoodTruckRepository
import com.blub.openstores.utils.DateUtils
import com.blub.openstores.utils.FragmentType
import com.blub.openstores.utils.SingleLiveEvent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Named


class FoodTruckViewModel @ViewModelInject constructor(
    private val foodTruckRepository: FoodTruckRepository,
    private val dateUtils: DateUtils,
    @Assisted @Named("io") private val dispatcher: CoroutineDispatcher
) : ViewModel() {
    private val coroutineExceptionHandler = CoroutineExceptionHandler{ _, ex ->
        ex.printStackTrace()
        errorShown.postValue(ex.message)
        isLoadingLiveData.postValue(false)
    }
    private var lastCheckedTime = 0L

    val foodTruckListLiveData: MutableLiveData<List<FoodTruck>> = MutableLiveData<List<FoodTruck>>()
    val fragmentLiveData: MutableLiveData<FragmentType> = MutableLiveData()
    val menuTitleLiveData: MutableLiveData<Int> = MutableLiveData()
    val isLoadingLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val selectedFoodTruck: MutableLiveData<FoodTruck> = MutableLiveData()
    val errorShown: SingleLiveEvent<String> = SingleLiveEvent()

    fun onIntent(intent: Intent) {
        when (intent) {
            InitIntent -> setToListFragment()
            StartIntent -> fetchAndHandleFoodTrucks()
            RefreshIntent -> fetchAndHandleFoodTrucks(true)
            FragmentToggleIntent -> toggleFragment()
            is MarkerSelectedIntent -> showFoodTruckDetails(intent.foodTruck)
            NonMarkerSelectedIntent -> showFoodTruckDetails(null)
        }
    }

    private fun showFoodTruckDetails(foodTruck: FoodTruck?) {
        selectedFoodTruck.value = foodTruck
    }

    private fun setToListFragment() {
        menuTitleLiveData.value = FragmentType.FOODTRUCK_MAP.title
        fragmentLiveData.value = FragmentType.FOODTRUCK_LIST
    }

    private fun fetchAndHandleFoodTrucks(manualRefresh: Boolean = false) {
        isLoadingLiveData.value = true

        viewModelScope.launch(dispatcher + coroutineExceptionHandler) {
            val sortedFoodTruckList = foodTruckRepository
                .getFoodTrucks(manualRefresh || periodicUpdate())
                .filter{ isStoreOpen(it) }
                .sortedWith(compareBy({ it.applicant }, { it.locationid }, { it.dayorder }))

            if (sortedFoodTruckList != foodTruckListLiveData.value) {
                foodTruckListLiveData.postValue(sortedFoodTruckList)
            }

            if (!sortedFoodTruckList.contains(selectedFoodTruck.value)) {
                selectedFoodTruck.postValue(null)
            }

            isLoadingLiveData.postValue(false)
        }
    }

    fun isStoreOpen(foodTruck: FoodTruck): Boolean {
        if (foodTruck.start24 == null || foodTruck.end24 == null) return false

        val today = dateUtils.getDayOfWeek().toString()
        val yesterday = (dateUtils.getDayOfWeek() - 1).toString() //account for stores open after midnight

        if (foodTruck.dayorder != today && foodTruck.dayorder != yesterday) return false

        return dateUtils.withinHours(foodTruck.dayorder == yesterday,
                    foodTruck.start24,
                    foodTruck.end24)
    }

    private fun periodicUpdate(): Boolean {
        val currentTime = System.currentTimeMillis()
        val shouldUpdate = currentTime - lastCheckedTime > TimeUnit.DAYS.toMillis(1)

        lastCheckedTime = currentTime

        return shouldUpdate

    }

    private fun toggleFragment() {
        if (fragmentLiveData.value == FragmentType.FOODTRUCK_LIST) {
            fragmentLiveData.value = FragmentType.FOODTRUCK_MAP
            menuTitleLiveData.value = FragmentType.FOODTRUCK_LIST.title
        } else {
            fragmentLiveData.value = FragmentType.FOODTRUCK_LIST
            menuTitleLiveData.value = FragmentType.FOODTRUCK_MAP.title
        }
    }
}