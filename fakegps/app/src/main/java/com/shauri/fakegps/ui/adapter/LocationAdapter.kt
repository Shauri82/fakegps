package com.shauri.fakegps.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shauri.fakegps.R
import com.shauri.fakegps.database.entity.Location

class LocationAdapter(val listener: OnLocationClickedListener) :
    RecyclerView.Adapter<LocationViewHolder>() {

    val locations: MutableList<Location> = ArrayList()
    //val listener: OnAdapterItemClickListener<PassengerAccount>? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val v: View? = inflaterFrom(parent)?.inflate(R.layout.row_location, parent, false)
        return LocationViewHolder(v!!)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val location: Location = locations[position]
        holder.bind(location, listener)
    }


    override fun getItemCount(): Int {
        return locations.size
    }

    fun update(loc: List<Location>) {
        locations.clear()
        locations.addAll(loc)
        notifyDataSetChanged()
    }

    open fun inflaterFrom(v: View): LayoutInflater? {
        return LayoutInflater.from(v.context)
    }

    interface OnLocationClickedListener {
        fun onLocationDeleteClicked(location: Location)
        fun onLocationClicked()
    }
}