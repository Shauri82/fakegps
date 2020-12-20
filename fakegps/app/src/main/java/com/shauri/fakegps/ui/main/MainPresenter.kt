package com.shauri.fakegps.ui.main

import android.view.View
import com.huawei.hms.maps.model.LatLng
import com.shauri.fakegps.BuildConfig
import com.shauri.fakegps.FirebaseConfig
import com.shauri.fakegps.R
import com.shauri.fakegps.data.ServiceData
import com.shauri.fakegps.dependency.AppComponent
import com.shauri.fakegps.needUpgrade
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
    private val firebase = FirebaseConfig()
    private var mocking = false

    fun onItemSelected(id: Int) {
        when (id) {
            R.id.nav_about -> router.goToAboutScreen()
            R.id.nav_settings -> router.goToSettingsScreen()
            R.id.nav_locations -> router.goToLocationsScreen()
            else -> {
            }
        }
        ui()?.closeMenu()
    }

    fun checkVersion() {
        if (needUpgrade(BuildConfig.VERSION_NAME, firebase.getMinVer())) {
            ui()?.showUpgradeDialog()
        } else {
            ui()?.setupPermissions()
        }
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

    fun setMockingStoped(){
        mocking = false
        onMockingChanged(null)
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
                                    ui()?.showSaveError(R.string.activityMain_save_location_error)
                                }
                            }, object : Consumer<Throwable> {
                                override fun accept(t: Throwable?) {
                                    ui()?.showSaveError(R.string.activityMain_save_location_error)
                                }
                            })
                    )
                }
            })
        }
    }

    fun onButtonClicked(position: LatLng?){
        mocking = !mocking
        onMockingChanged(position)
    }

    fun onMockingChanged(position: LatLng?) {
        if (mocking) {
            ui()?.setPauseButton()
            ui()?.setButtonText(R.string.stop)
            ui()?.setButtonBackground(R.color.colorRed)
            ui()?.setPinVisibility(View.GONE)
            startMocking(position)
        } else {
            ui()?.setPinVisibility(View.VISIBLE)
            stopService()
            ui()?.setPlayButton()
            ui()?.setButtonText(R.string.start)
            ui()?.setButtonBackground(R.color.colorGreen)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}