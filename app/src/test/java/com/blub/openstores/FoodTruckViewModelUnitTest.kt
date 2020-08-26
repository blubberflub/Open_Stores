package com.blub.openstores

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.blub.openstores.model.FoodTruck
import com.blub.openstores.repository.FoodTruckRepository
import com.blub.openstores.utils.DateUtils
import com.blub.openstores.view.foodtruck.FoodTruckViewModel
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.Spy
import org.mockito.Mockito.`when` as whenever

@ExperimentalCoroutinesApi
class FoodTruckViewModelUnitTest {
    @Mock
    lateinit var repository: FoodTruckRepository

    @Spy
    var dateUtils: DateUtils = DateUtils()

    private val dispatcher = TestCoroutineDispatcher()
    private val scope = TestCoroutineScope(dispatcher)

    lateinit var viewModel: FoodTruckViewModel

    @Rule
    @JvmField
    var rule: TestRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(dispatcher)
        viewModel = FoodTruckViewModel(repository, dateUtils, dispatcher)
    }

    @After
    fun cleanup() {
        Dispatchers.resetMain()
        scope.cleanupTestCoroutines()
    }

    @Test
    fun testStillOpenNextDay() {
        val foodTruck = FoodTruck(dayorder = "2", start24 = "23:00", end24 = "04:00")

        whenever(dateUtils.getCurrentTime()).thenReturn("01:00")
        whenever(dateUtils.getDayOfWeek()).thenReturn(3)

        val actual = viewModel.isStoreOpen(foodTruck)
        assertEquals(true, actual)
    }

    @Test
    fun testStillOpenNextDay_midnight() {
        val foodTruck = FoodTruck(dayorder = "2", start24 = "23:00", end24 = "24:00")

        whenever(dateUtils.getCurrentTime()).thenReturn("01:00")
        whenever(dateUtils.getDayOfWeek()).thenReturn(3)

        val actual = viewModel.isStoreOpen(foodTruck)
        assertEquals(false, actual)
    }

    @Test
    fun testYesterday_notOpen() {
        val foodTruck = FoodTruck(dayorder = "2", start24 = "15:00", end24 = "22:00")

        whenever(dateUtils.getCurrentTime()).thenReturn("17:00")
        whenever(dateUtils.getDayOfWeek()).thenReturn(3)

        val actual = viewModel.isStoreOpen(foodTruck)
        assertEquals(false, actual)
    }
}