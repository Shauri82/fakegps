package com.shauri.fakegps.ui.router

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import com.huawei.hms.maps.model.LatLng
import com.shauri.fakegps.BuildConfig
import com.shauri.fakegps.GpsService
import com.shauri.fakegps.data.ServiceData
import com.shauri.fakegps.ui.about.AboutActivity
import com.shauri.fakegps.ui.locations.LocationsActivity
import com.shauri.fakegps.ui.move.MoveActivity
import com.shauri.fakegps.ui.settings.SettingsActivity


val SERVICE_DATA = "service_data"


class Router(val activity: AppCompatActivity) {
    fun goToMoveScreen() {
        val i = Intent(activity, MoveActivity::class.java)
        activity.startActivity(i)
    }

    fun goToSettingsScreen() {
        val i = Intent(activity, SettingsActivity::class.java)
        activity.startActivity(i)
    }

    fun goToAboutScreen() {
        val i = Intent(activity, AboutActivity::class.java)
        activity.startActivity(i)
    }

    fun goToLocationsScreen() {
        val i = Intent(activity, LocationsActivity::class.java)
        activity.startActivityForResult(i, REQUEST_LOCATION)
    }

    fun startService(data: ServiceData) {
        var intent = Intent(activity, GpsService::class.java)
        intent.putExtra(SERVICE_DATA, data)
        activity.startService(intent)
    }

    fun stopService() {
        activity.stopService(
            Intent(activity, GpsService::class.java)
        )
    }

    fun closeWithLocation(point: LatLng) {
        val intent = Intent()
        intent.putExtra(KEY_LOCATION, point)
        activity.setResult(Activity.RESULT_OK, intent)
        activity.finish()
    }

    fun gotoAppGallery() {
        try {
            val intent =
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("appmarket://details?id=" + BuildConfig.APPLICATION_ID)
                )
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            activity.startActivity(intent)
            activity.finish()
        } catch (anfe: ActivityNotFoundException) {

        }
    }
}