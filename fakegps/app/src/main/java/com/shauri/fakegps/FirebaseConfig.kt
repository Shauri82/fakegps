package com.shauri.fakegps

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import timber.log.Timber

class FirebaseConfig (){

    private val FETCH_INTERVAL_IN_SEC = if (BuildConfig.DEBUG) 0L else 3600L

    private var remoteConfig: FirebaseRemoteConfig? = null


    init {
        try {
            remoteConfig = Firebase.remoteConfig
            val configSettings = remoteConfigSettings {
                minimumFetchIntervalInSeconds = FETCH_INTERVAL_IN_SEC
            }
            remoteConfig!!.setConfigSettingsAsync(configSettings)
            remoteConfig!!.setDefaultsAsync(R.xml.remote_config_defaults)
            loadRemoteParameters()
        } catch (e: IllegalStateException) {
            Timber.d(e)
        }
    }


    private fun loadRemoteParameters() {
        remoteConfig!!.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val updated = task.result
                    Timber.d("Config params updated: $updated")
                }
            }
    }


    fun getMinVer(): String? {
        return remoteConfig?.getString("min_ver");
    }
}
