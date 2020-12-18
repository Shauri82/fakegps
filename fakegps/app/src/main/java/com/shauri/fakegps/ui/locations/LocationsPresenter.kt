package com.shauri.fakegps.ui.locations

import android.util.Log
import com.shauri.fakegps.R
import com.shauri.fakegps.database.entity.Location
import com.shauri.fakegps.dependency.AppComponent
import com.shauri.fakegps.ui.adapter.LocationAdapter
import com.shauri.fakegps.ui.base.BasePresenter
import com.shauri.fakegps.ui.dialog.AlertDialog
import com.shauri.fakegps.ui.router.Router
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.functions.Action
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.schedulers.Schedulers

class LocationsPresenter(uiRef: LocationsUi, router: Router, appComponent: AppComponent?) :
    BasePresenter<LocationsUi>(uiRef, router, appComponent) {
    val dataInteractor = appComponent?.dataInteractor()
    val compositeDisposable = CompositeDisposable()


    override fun onCreate() {
        super.onCreate()
        ui()?.prepareList(object : LocationAdapter.OnLocationClickedListener {
            override fun onLocationDeleteClicked(location: Location) {
                onDeleteLocationClicked(location)
            }

            override fun onLocationClicked() {

            }
        })
        loadLocations()
    }

    fun loadLocations() {
        compositeDisposable.add(dataInteractor?.getLocations()?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(object : Consumer<List<Location>> {
                override fun accept(t: List<Location>?) {
                    if (t != null) {
                        ui()?.setLocations(t)
                    }
                }

            }, object : Consumer<Throwable> {
                override fun accept(t: Throwable?) {
                    Log.e("CCC", "exc", t)

                }
            }
            ))
    }


    fun onDeleteLocationClicked(location: Location) {
        ui()?.showConfirmDialog(
            R.string.dialog_delete_location,
            location.name,
            object : AlertDialog.OnSaveClickedListener {
                override fun onConfirmClicked() {
                    deleteLocation(location)
                }
            })
    }

    fun deleteLocation(location: Location) {

        compositeDisposable.add(
            dataInteractor?.deleteLocation(location)

                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(object : Action {
                    override fun run() {
                        loadLocations()
                    }
                }, object : Consumer<Throwable> {
                    override fun accept(t: Throwable?) {
                        Log.e("CCC", "exc", t)
                    }
                })
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}