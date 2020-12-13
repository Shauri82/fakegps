package com.shauri.fakegps.ui.base

import com.shauri.fakegps.dependency.AppComponent
import com.shauri.fakegps.ui.router.Router
import java.lang.ref.WeakReference

abstract class BasePresenter<Ui : BaseUi>(uiRef: Ui, val router: Router,val appComponent: AppComponent?) {
    val ui = WeakReference<Ui>(uiRef)

    protected open fun ui(): Ui? {
        return ui.get()
    }

    protected open fun router(): Router? {
        return router
    }

    open fun onCreate() {
    }

    open fun onResume() {
    }

    open fun onPause() {
    }

    open fun onDestroy() {}
}