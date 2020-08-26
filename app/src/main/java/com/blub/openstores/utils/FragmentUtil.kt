package com.blub.openstores.utils

import android.content.Context
import android.content.res.Resources
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.blub.openstores.R
import com.blub.openstores.view.foodtruck.list.FoodTruckListFragment
import com.blub.openstores.view.foodtruck.map.FoodTruckMapFragment
import javax.inject.Inject

class FragmentUtil @Inject constructor(
    private val foodtruckListFragment: FoodTruckListFragment,
    private val foodTruckMapFragment: FoodTruckMapFragment
) {

    fun setFragment(fragmentType: FragmentType, fragmentManager: FragmentManager, container: Int) {
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()

        val fragment = when (fragmentType) {
            FragmentType.FOODTRUCK_LIST -> foodtruckListFragment
            FragmentType.FOODTRUCK_MAP -> foodTruckMapFragment
        }

        if (fragmentManager.findFragmentByTag(fragmentType.name) != null) {
            fragmentTransaction.hide(fragmentManager.fragments.last())
            fragmentTransaction.show(fragmentManager.findFragmentByTag(fragmentType.name)!!)
        } else {
            fragmentTransaction.add(container, fragment, fragmentType.name)
        }

        fragmentTransaction.commit()
    }
}

enum class FragmentType(val title: Int) {
    FOODTRUCK_LIST(R.string.list),
    FOODTRUCK_MAP(R.string.map)
}