package com.shauri.fakegps.domain.interactor

import android.content.SharedPreferences
import javax.inject.Inject

class PrefsStorage @Inject constructor(private val prefs: SharedPreferences) {
    var isMockMovement: Boolean
        get() = prefs.getBoolean(KEY_MOCK_MOVEMENT, false)
        set(mock) {
            prefs.edit().putBoolean(KEY_MOCK_MOVEMENT, mock).apply()
        }

    var isRandomMovement: Boolean
        get() = prefs.getBoolean(KEY_MOCK_RANDOM, false)
        set(mock) {
            prefs.edit().putBoolean(KEY_MOCK_RANDOM, mock).apply()
        }

    var arc: Double
        get() = prefs.getFloat(KEY_MOCK_ARC, 0.0f).toDouble()
        set(value) {
            prefs.edit().putFloat(KEY_MOCK_ARC, value.toFloat()).apply()
        }

    var accuracy: Int
        get() = prefs.getInt(KEY_ACCURACY, 1)
        set(value) {
            prefs.edit().putInt(KEY_ACCURACY, value).apply()
        }
    var interval: Int
        get() = prefs.getInt(KEY_INTERVAL, 1)
        set(value) {
            prefs.edit().putInt(KEY_INTERVAL, value).apply()
        }

    var gms: Boolean
        get() = prefs.getBoolean(KEY_GMS, false)
        set(mock) {
            prefs.edit().putBoolean(KEY_GMS, mock).apply()
        }

    var hms: Boolean
        get() = prefs.getBoolean(KEY_HMS, true)
        set(mock) {
            prefs.edit().putBoolean(KEY_HMS, mock).apply()
        }

    companion object {
        private const val KEY_MOCK_MOVEMENT = "mock_movement"
        private const val KEY_MOCK_RANDOM = "mock_random"
        private const val KEY_MOCK_ARC = "mock_arc"
        private const val KEY_ACCURACY = "mock_accuracy"
        private const val KEY_INTERVAL = "mock_interval"
        private const val KEY_GMS = "mock_gms"
        private const val KEY_HMS = "mock_hms"


    }
}