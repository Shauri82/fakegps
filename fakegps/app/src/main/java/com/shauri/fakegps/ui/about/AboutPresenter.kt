package com.shauri.fakegps.ui.about

import com.shauri.fakegps.BuildConfig
import com.shauri.fakegps.dependency.AppComponent
import com.shauri.fakegps.ui.base.BasePresenter
import com.shauri.fakegps.ui.locations.LocationsUi
import com.shauri.fakegps.ui.router.Router

class AboutPresenter (uiRef: AboutUi, router: Router, appComponent: AppComponent?) :
    BasePresenter<AboutUi>(uiRef, router, appComponent) {

    override fun onCreate() {
        super.onCreate()
        ui()?.setVersion(BuildConfig.VERSION_NAME)
    }
}