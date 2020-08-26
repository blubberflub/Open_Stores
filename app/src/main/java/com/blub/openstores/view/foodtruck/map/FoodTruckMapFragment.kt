package com.blub.openstores.view.foodtruck.map

import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.blub.openstores.R
import com.blub.openstores.model.FoodTruck
import com.blub.openstores.utils.MoshiUtils
import com.blub.openstores.view.foodtruck.FoodTruckViewModel
import com.blub.openstores.view.foodtruck.MarkerSelectedIntent
import com.blub.openstores.view.foodtruck.NonMarkerSelectedIntent
import com.blub.openstores.view.foodtruck.list.FoodTruckViewHolder
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior


class FoodTruckMapFragment : Fragment(R.layout.fragment_foodtruckmap), OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
    GoogleMap.OnMapLoadedCallback {
    private lateinit var bottomSheetView: View
    private lateinit var map: GoogleMap
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private val viewModel: FoodTruckViewModel by viewModels(
        ownerProducer = { requireActivity() }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.foodtruck_map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        val bottomSheetLayout: ConstraintLayout = view.findViewById(R.id.bottomSheet)
        bottomSheetView = view.findViewById(R.id.foodtruck_map_details)

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout).apply {
            isFitToContents = true
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap.apply {
            val sf = LatLng(37.7749, -122.4194)
            moveCamera(CameraUpdateFactory.newLatLng(sf))

            setOnMapLoadedCallback(this@FoodTruckMapFragment)
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        val foodTruck: FoodTruck? = MoshiUtils.fromJson<FoodTruck>(marker.snippet)

        foodTruck?.let {
            viewModel.onIntent(MarkerSelectedIntent(foodTruck))
        }

        return true
    }

    override fun onMapLoaded() {
        map.setOnMapClickListener {
            viewModel.onIntent(NonMarkerSelectedIntent)
        }

        viewModel.foodTruckListLiveData.observe(requireActivity()) {
            val latlngBuilder = LatLngBounds.Builder()

            it.forEach {
                val marker = MarkerOptions().apply {
                    if (it.getLatLong() == null) {
                        return@forEach
                    }

                    position(it.getLatLong()!!)
                    snippet(MoshiUtils.toJson(it))
                    latlngBuilder.include(position)
                }

                map.addMarker(marker)
            }

            map.setOnMarkerClickListener(this)

            val bounds = latlngBuilder.build()
            val padding = resources.getInteger(R.integer.map_padding)
            val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding)
            map.animateCamera(cameraUpdate)
        }

        viewModel.selectedFoodTruck.observe(requireActivity()) {
            if (it == null) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                return@observe
            }

            FoodTruckViewHolder(bottomSheetView).bindView(it)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

            val cameraUpdate = it.getLatLong()
            val typedValue = TypedValue()
            resources.getValue(R.dimen.map_zoom, typedValue, true)
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(cameraUpdate, typedValue.float))
        }
    }
}