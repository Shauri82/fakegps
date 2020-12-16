package com.shauri.fakegps.ui.main

import com.huawei.hms.maps.model.LatLng
import com.shauri.fakegps.R
import com.shauri.fakegps.data.ServiceData
import com.shauri.fakegps.dependency.AppComponent
import com.shauri.fakegps.ui.base.BasePresenter
import com.shauri.fakegps.ui.router.Router

class MainPresenter(uiRef: MainUi, router: Router, appComponent: AppComponent?) :
    BasePresenter<MainUi>(uiRef, router, appComponent) {
    val prefsInteractor = appComponent?.prefsInteractor()
    fun onItemSelected(id: Int) {
        when (id) {
            R.id.nav_home -> router.goToMoveScreen()
            R.id.nav_settings -> router.goToSettingsScreen()
            else -> {
            }
        }
        ui()?.closeMenu()
    }

    fun startMocking(point: LatLng) {
        val data = ServiceData(
            point = point,
            mockMove = prefsInteractor?.isMockMovement() ?: false,
            randomMove = prefsInteractor?.isRandomMovement() ?: false,
            direction = prefsInteractor?.getArc(),
            gms = prefsInteractor?.isGms() ?: false,
            hms = prefsInteractor?.isHms() ?: false
        )
        router.startService(data)
    }

    fun stopService(){
        router.stopService()
    }
}