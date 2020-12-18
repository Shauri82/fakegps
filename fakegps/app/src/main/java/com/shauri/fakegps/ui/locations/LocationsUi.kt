package com.shauri.fakegps.ui.locations

import androidx.annotation.StringRes
import com.shauri.fakegps.database.entity.Location
import com.shauri.fakegps.ui.adapter.LocationAdapter
import com.shauri.fakegps.ui.base.BaseUi
import com.shauri.fakegps.ui.dialog.AlertDialog

interface LocationsUi : BaseUi {

    fun setLocations(locations: List<Location>)

    fun prepareList(listener: LocationAdapter.OnLocationClickedListener)

    fun showConfirmDialog(@StringRes title:Int,locationName: String, listener: AlertDialog.OnSaveClickedListener)
}