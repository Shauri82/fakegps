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


    companion object {
        private const val KEY_MOCK_MOVEMENT = "mock_movement"
        private const val KEY_MOCK_RANDOM = "mock_random"
        private const val KEY_MOCK_ARC = "mock_arc"

    }
}