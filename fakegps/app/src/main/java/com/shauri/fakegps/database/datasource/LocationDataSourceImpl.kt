package com.shauri.fakegps.database.datasource

import com.shauri.fakegps.database.dao.LocationDao
import com.shauri.fakegps.database.entity.Location
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class LocationDataSourceImpl @Inject constructor(val dao: LocationDao) : LocationDataSource {
    override fun saveLocation(location: Location): Completable {
        return Completable.fromAction { dao.insertLocation(location) }
    }

    override fun getLocations(): Single<List<Location>> {
        return Single.fromCallable {
            dao.getLocations()
        }
    }

    override fun deleteLocation(location:Location): Completable {
        return Completable.fromAction { dao.deleteLocation(location) }
    }
}