package com.shauri.fakegps.ui.router

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.shauri.fakegps.GpsService
import com.shauri.fakegps.data.ServiceData
import com.shauri.fakegps.ui.move.MoveActivity
import com.shauri.fakegps.ui.settings.SettingsActivity

val SERVICE_DATA="service_data"


class Router(val activity: AppCompatActivity) {
    fun goToMoveScreen() {
        val i = Intent(activity, MoveActivity::class.java)
        activity.startActivity(i)
    }

    fun goToSettingsScreen() {
        val i = Intent(activity, SettingsActivity::class.java)
        activity.startActivity(i)
    }

    fun startService(data:ServiceData){
        var intent = Intent(activity, GpsService::class.java)
        intent.putExtra(SERVICE_DATA, data)
        activity.startService(intent)
    }

    fun stopService(){
        activity.stopService(
            Intent(activity, GpsService::class.java)
        )
    }

}