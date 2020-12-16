package com.shauri.fakegps.ui.settings

import com.shauri.fakegps.dependency.AppComponent
import com.shauri.fakegps.ui.base.BasePresenter
import com.shauri.fakegps.ui.router.Router

class SettingsPresenter(uiRef: SettingsUi, router: Router, appComponent: AppComponent?) :
    BasePresenter<SettingsUi>(uiRef, router, appComponent) {

    val prefsInteractor = appComponent?.prefsInteractor()

    override fun onCreate() {
        super.onCreate()
        ui()?.setAccuracy(prefsInteractor?.getAccuracy() ?: 1)
        ui()?.setInterval(prefsInteractor?.getInterval() ?: 1)
    }

    fun afterAvailabilityChecked(googlePlayServicesAvailable:Boolean, huaweiServicesAvailable:Boolean){
        if(googlePlayServicesAvailable){
            ui()?.enableGooglePlayServices()
            ui()?.setGmsChecked(prefsInteractor?.isGms() ?: false)
        }
        else{
            ui()?.disableGooglePlayServices()
        }
        if(huaweiServicesAvailable){
            ui()?.enableHuaweiServices()
            ui()?.setHmsChecked(prefsInteractor?.isHms() ?: false)
        }
        else{
            ui()?.disableHuaweiPlayServices()
        }
    }

    fun onHmsCheckChanged(check:Boolean){
        prefsInteractor?.setHms(check)
    }

    fun onGmsCheckChanged(check:Boolean){
        prefsInteractor?.setGms(check)
    }
}