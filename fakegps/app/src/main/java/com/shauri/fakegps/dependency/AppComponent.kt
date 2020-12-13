package com.shauri.fakegps.dependency

import com.shauri.fakegps.dependency.module.AppModule
import com.shauri.fakegps.domain.interactor.PrefsInteractor
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun prefsInteractor(): PrefsInteractor?
}
