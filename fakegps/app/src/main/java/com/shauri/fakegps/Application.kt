package com.shauri.fakegps

import android.app.Application
import android.content.Context
import com.shauri.fakegps.dependency.AppComponent
import com.shauri.fakegps.dependency.DaggerAppComponent
import com.shauri.fakegps.dependency.module.AppModule


class Application : Application() {
    private var component: AppComponent? = null

    override fun onCreate() {
        super.onCreate()
        initDagger()
    }


    private fun initDagger() {
        if (component == null) {
            component = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
        }
    }

    companion object {
        fun getComponentFrom(context: Context): AppComponent? {
            val app = context.applicationContext as com.shauri.fakegps.Application
            return app.component
        }
    }

}