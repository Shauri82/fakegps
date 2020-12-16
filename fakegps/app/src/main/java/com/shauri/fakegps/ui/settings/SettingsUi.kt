package com.shauri.fakegps.ui.settings

import com.shauri.fakegps.ui.base.BaseUi

interface SettingsUi : BaseUi {
    fun setInterval(interval:Int)
    fun setAccuracy(accuracy:Int)
    fun enableGooglePlayServices()
    fun enableHuaweiServices()
    fun disableGooglePlayServices()
    fun disableHuaweiPlayServices()
    fun setGmsChecked(checked:Boolean)
    fun setHmsChecked(checked:Boolean)
}