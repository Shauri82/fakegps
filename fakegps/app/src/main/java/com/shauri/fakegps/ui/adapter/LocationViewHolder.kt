package com.shauri.fakegps.ui.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.shauri.fakegps.database.entity.Location
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.row_location.view.*

class LocationViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
    LayoutContainer {

    fun bind(location: Location, listener: LocationAdapter.OnLocationClickedListener) {
        with(containerView) {
            rowLocation_tvName.setText(location.name)
            rowLocation_tvLatLng.text = "${location.lat},${location.lon}"
            rowLocation_ivDelete.setOnClickListener { listener.onLocationDeleteClicked(location) }
            rowLocation_container.setOnClickListener { listener.onLocationClicked(location) }
        }
    }
}