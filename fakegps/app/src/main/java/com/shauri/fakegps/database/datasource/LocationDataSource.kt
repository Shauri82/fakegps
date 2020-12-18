package com.shauri.fakegps.database.datasource

import com.shauri.fakegps.database.entity.Location
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface LocationDataSource {
    fun saveLocation(location:Location):Completable
    fun getLocations(): Single<List<Location>>
    fun deleteLocation(location:Location):Completable
}