package com.shauri.fakegps.ui.settings

import androidx.annotation.NonNull
import androidx.annotation.StringRes
import com.shauri.fakegps.ui.base.BaseUi
import com.shauri.fakegps.ui.dialog.InputDialog

interface SettingsUi : BaseUi {
    fun setInterval(interval: Int)
    fun setAccuracy(accuracy: Int)
    fun enableGooglePlayServices()
    fun enableHuaweiServices()
    fun disableGooglePlayServices()
    fun disableHuaweiPlayServices()
    fun setGmsChecked(checked: Boolean)
    fun setHmsChecked(checked: Boolean)

    fun openDialog(
        @StringRes label: Int,
        @NonNull value: String,
        @NonNull maxValue:Int,
        listener: InputDialog.OnSaveClickedListener
    )
}