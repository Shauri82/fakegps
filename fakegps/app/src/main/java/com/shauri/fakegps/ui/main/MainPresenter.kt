package com.shauri.fakegps.ui.main

import android.util.Log
import com.huawei.hms.maps.model.LatLng
import com.shauri.fakegps.R
import com.shauri.fakegps.data.ServiceData
import com.shauri.fakegps.dependency.AppComponent
import com.shauri.fakegps.ui.base.BasePresenter
import com.shauri.fakegps.ui.dialog.InputStringDialog
import com.shauri.fakegps.ui.router.Router
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.functions.Action
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.schedulers.Schedulers

class MainPresenter(uiRef: MainUi, router: Router, appComponent: AppComponent?) :
    BasePresenter<MainUi>(uiRef, router, appComponent) {
    val prefsInteractor = appComponent?.prefsInteractor()
    val dataInteractor = appComponent?.dataInteractor()
    val compositeDisposable = CompositeDisposable()


    fun onItemSelected(id: Int) {
        when (id) {
            R.id.nav_home -> router.goToMoveScreen()
            R.id.nav_settings -> router.goToSettingsScreen()
            R.id.nav_locations -> router.goToLocationsScreen()
            else -> {
            }
        }
        ui()?.closeMenu()
    }

    fun startMocking(point: LatLng?) {
        val data = ServiceData(
            point = point,
            mockMove = prefsInteractor?.isMockMovement() ?: false,
            randomMove = prefsInteractor?.isRandomMovement() ?: false,
            direction = prefsInteractor?.getArc(),
            accuracy = prefsInteractor?.getAccuracy() ?: 1,
            interval = prefsInteractor?.getInterval() ?: 1,
            gms = prefsInteractor?.isGms() ?: false,
            hms = prefsInteractor?.isHms() ?: false
        )
        router.startService(data)
    }

    fun stopService() {
        router.stopService()
    }

    fun onSaveLocationClicked(point: LatLng?) {
        if (point != null) {
            ui()?.showDialog(object : InputStringDialog.OnSaveClickedListener {
                override fun onSaveClicked(value: String) {
                    compositeDisposable.add(
                        dataInteractor?.saveLocation(point, value)
                            ?.subscribeOn(Schedulers.io())
                            ?.observeOn(AndroidSchedulers.mainThread())
                            ?.subscribe(object : Action {
                                override fun run() {
                                    Log.d("CCC", "saved")
                                }
                            }, object : Consumer<Throwable> {
                                override fun accept(t: Throwable?) {
                                    Log.e("CCC", "exc", t)
                                }
                            })
                    )
                }
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}