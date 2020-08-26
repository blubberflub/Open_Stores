package com.blub.openstores.view.foodtruck

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.blub.openstores.R
import com.blub.openstores.databinding.ActivityFoodtruckBinding
import com.blub.openstores.utils.FragmentUtil
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class FoodTruckActivity : AppCompatActivity() {
    private val viewModel: FoodTruckViewModel by viewModels()
    @Inject lateinit var fragmentUtil: FragmentUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityFoodtruckBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_foodtruck
        )
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        viewModel.errorShown.observe(this) {
            Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT).show()
        }

        viewModel.fragmentLiveData.observe(this) {
            fragmentUtil.setFragment(it, supportFragmentManager, R.id.container)
        }

        if (savedInstanceState != null) return

        viewModel.onIntent(InitIntent)
    }

    override fun onStart() {
        super.onStart()

        viewModel.onIntent(StartIntent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_foodtruck, menu)

        viewModel.menuTitleLiveData.observe(this) {
            val optionTitle = menu?.findItem(R.id.foodtruck_fragment_option)
            optionTitle?.title = resources.getString(it)
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.foodtruck_fragment_option -> viewModel.onIntent(FragmentToggleIntent)
        }

        return super.onOptionsItemSelected(item)
    }
}