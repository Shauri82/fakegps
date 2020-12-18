package com.shauri.fakegps.dependency

import com.shauri.fakegps.dependency.module.AppModule
import com.shauri.fakegps.dependency.module.RoomModule
import com.shauri.fakegps.domain.interactor.DataInteractor
import com.shauri.fakegps.domain.interactor.PrefsInteractor
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, RoomModule::class])
interface AppComponent {
    fun prefsInteractor(): PrefsInteractor?
    fun dataInteractor(): DataInteractor?
}
