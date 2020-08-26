package com.blub.openstores.view.foodtruck.list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.blub.openstores.R
import com.blub.openstores.view.foodtruck.FoodTruckViewModel
import com.blub.openstores.view.foodtruck.RefreshIntent
import kotlinx.android.synthetic.main.fragment_foodtrucklist.*

class FoodTruckListFragment : Fragment(R.layout.fragment_foodtrucklist) {
    private val viewAdapter: FoodTruckListAdapter by lazy { FoodTruckListAdapter() }
    private val viewModel: FoodTruckViewModel by viewModels(
        ownerProducer = { requireActivity() }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(foodtruck_recycler_view) {
            layoutManager = LinearLayoutManager(context)
            adapter = viewAdapter
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        }

        val refresh: SwipeRefreshLayout = view.findViewById(R.id.refreshView)
        refresh.setOnRefreshListener {
            viewModel.onIntent(RefreshIntent)
        }

        viewModel.foodTruckListLiveData.observe(requireActivity()) {
            viewAdapter.submitList(it)
        }

        viewModel.isLoadingLiveData.observe(requireActivity()) {
            refresh.isRefreshing = it
        }
    }
}