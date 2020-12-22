package com.shauri.fakegps.domain.interactor

import com.huawei.hms.maps.model.LatLng
import com.shauri.fakegps.database.datasource.LocationDataSource
import com.shauri.fakegps.database.entity.Location
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class DataInteractor @Inject constructor(val locationDataSource: LocationDataSource) {

    fun saveLocation(point: LatLng, name: String): Completable = locationDataSource.saveLocation(
        Location(
            lat = point.latitude,
            lon = point.longitude,
            name = name
        )
    )

    fun getLocations() = locationDataSource.getLocations()

    fun deleteLocation(location:Location) = locationDataSource.deleteLocation(location)

    fun countLocations() = locationDataSource.countLocations()
}