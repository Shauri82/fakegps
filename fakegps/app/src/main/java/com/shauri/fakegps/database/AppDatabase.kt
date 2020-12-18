package com.shauri.fakegps.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.shauri.fakegps.database.dao.LocationDao
import com.shauri.fakegps.database.entity.Location

@Database(entities = [Location::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun locationDao(): LocationDao
}