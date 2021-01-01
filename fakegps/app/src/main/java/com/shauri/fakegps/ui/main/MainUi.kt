package com.shauri.fakegps.ui.main

import android.location.Location
import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.huawei.hms.maps.model.LatLng
import com.shauri.fakegps.ui.base.BaseUi
import com.shauri.fakegps.ui.dialog.InputStringDialog

interface MainUi : BaseUi {
    fun closeMenu()
    fun showDialog(listener:InputStringDialog.OnSaveClickedListener)
    fun showSaveError(@StringRes error: Int)
    fun showUpgradeDialog()
    fun setupPermissions()
    fun goToLocation(lat: Double?, lon: Double?)
    fun setPauseButton()
    fun setPlayButton()
    fun setButtonText(@StringRes label:Int)
    fun setButtonBackground(@AttrRes background: Int)
    fun setPinVisibility(visibility: Int)
    fun showTooMuchLocations(max:Int)
    fun showToast(@StringRes label:Int)
}