package com.shauri.fakegps.database.dao

import androidx.room.*
import com.shauri.fakegps.database.entity.Location

@Dao
interface LocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLocation(location: Location)

    @Delete
    fun deleteLocation(location: Location)

    @Query("SELECT * FROM locations")
    fun getLocations(): List<Location>
}