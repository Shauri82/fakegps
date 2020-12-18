package com.shauri.fakegps.dependency.module

import android.content.Context
import androidx.room.Room
import com.shauri.fakegps.database.AppDatabase
import com.shauri.fakegps.database.dao.LocationDao
import com.shauri.fakegps.database.datasource.LocationDataSource
import com.shauri.fakegps.database.datasource.LocationDataSourceImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule {

    @Singleton
    @Provides
    fun providesAppDatabase(context: Context?): AppDatabase {
        return Room.databaseBuilder(context!!, AppDatabase::class.java, "shauriFakeGps.db")
            .build()
    }

    @Singleton
    @Provides
    fun providesLocationDao(database: AppDatabase): LocationDao {
        return database.locationDao()
    }

    @Singleton
    @Provides
    fun providesLocationDataSource(locationDao: LocationDao): LocationDataSource {
        return LocationDataSourceImpl(locationDao)
    }


}