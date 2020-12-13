package com.shauri.fakegps.dependency.module

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(context: Context) {
    private val context: Context

    @Provides
    fun context(): Context {
        return context
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(): SharedPreferences {
        return context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    companion object {
        const val SHARED_PREFERENCES_NAME = "shauriFakeGps"
    }

    init {
        this.context = context.applicationContext
    }
}