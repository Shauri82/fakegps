package com.shauri.fakegps.database.dao

import androidx.room.*
import com.shauri.fakegps.database.entity.Location

@Dao
interface LocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLocation(location: Location)

    @Delete
    fun deleteLocation(location: Location)

    @Query("SELECT * FROM locations ORDER BY id desc")
    fun getLocations(): List<Location>

    @Query("SELECT COUNT(id)  FROM locations")
    fun getLocationsCount(): Int
}