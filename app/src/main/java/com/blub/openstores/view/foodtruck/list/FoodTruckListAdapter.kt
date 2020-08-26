package com.blub.openstores.view.foodtruck.list

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.blub.openstores.R
import com.blub.openstores.model.FoodTruck

class FoodTruckListAdapter : ListAdapter<FoodTruck, FoodTruckViewHolder>(DiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodTruckViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_foodtruck, parent, false)

        return FoodTruckViewHolder(view)
    }

    override fun onBindViewHolder(holder: FoodTruckViewHolder, position: Int) {
        holder.bindView(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<FoodTruck>() {
        override fun areItemsTheSame(oldItem: FoodTruck, newItem: FoodTruck): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: FoodTruck, newItem: FoodTruck): Boolean =
            oldItem == newItem
    }
}

class FoodTruckViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val title: TextView = view.findViewById(R.id.title)
    private val address: TextView = view.findViewById(R.id.address)
    private val genres: TextView = view.findViewById(R.id.genres)
    private val hours: TextView = view.findViewById(R.id.hours)

    fun bindView(itemView: FoodTruck) {
        with (itemView) {
            bindString(title, applicant)
            bindString(address, locationdesc)
            bindString(genres, optionaltext)
            bindString(hours,"${itemView.starttime}-${itemView.endtime}")
        }
    }

    private fun bindString(view: TextView, text: String?) {
        if (!text.isNullOrBlank()) {
            view.text = text
            view.visibility = VISIBLE
        } else {
            view.visibility = GONE
        }
    }
}