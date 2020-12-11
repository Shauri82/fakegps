package com.shauri.fakegps

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.ui.AppBarConfiguration
import com.google.android.material.navigation.NavigationView
import com.huawei.hms.location.FusedLocationProviderClient
import com.huawei.hms.location.LocationServices
import com.huawei.hms.maps.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration

    private val testProvider = "gpstest"
    private val RECORD_REQUEST_CODE = 101
    private var mocking = false
    private val firebase = FirebaseConfig()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var huaweiMap: HuaweiMap

    val receiver = FakeGpsBroadcastReceiver(Runnable {
        mocking = false
        handleMocking()
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val filter = IntentFilter()
        filter.addAction(STOP_MOCKING_ACTION)
        this.registerReceiver(receiver, filter)
        checkVersion()
        play_button.setOnClickListener {
            mocking = !mocking
            handleMocking()
        }

        setSupportActionBar(toolbar)

        toolbar.setNavigationIcon(R.drawable.ic_baseline_menu_24)
        toolbar.setNavigationOnClickListener { v: View? ->
            drawerLayout.openDrawer(GravityCompat.START)
        }
        tvVersion.setText("v. "+BuildConfig.VERSION_NAME)
    }


    private fun checkVersion() {
        if (needUpgrade(BuildConfig.VERSION_NAME, firebase.getMinVer())) {
            AlertDialog.Builder(this)
                .setMessage(R.string.version)
                .setPositiveButton(
                    R.string.close
                ) { _, _ ->
                    finish()
                }
                .show()
        } else {
            setupPermissions()
        }
    }


    private fun startSevice() {
        var intent = Intent(this, GpsService::class.java)
        val point = huaweiMap.cameraPosition.target
        intent.putExtra("lat", point.latitude)
        intent.putExtra("lon", point.longitude)
        startService(intent)
    }

    private fun handleMocking() {
        if (mocking) {
            play_button.setCompoundDrawablesRelativeWithIntrinsicBounds(
                ResourcesCompat.getDrawable(
                    getResources(),
                    R.drawable.ic_baseline_pause_24,
                    null
                ), null, null, null
            );
            play_button.setText(R.string.stop)
            play_button.setBackgroundResource(R.color.colorRed)
            startSevice()
            Log.d("CCC", "mocking stared")
        } else {
            stopService(
                Intent(this, GpsService::class.java)
            )
            Log.d("CCC", "mocking stoped")
            play_button.setCompoundDrawablesRelativeWithIntrinsicBounds(
                ResourcesCompat.getDrawable(
                    getResources(),
                    R.drawable.ic_baseline_play_arrow_24,
                    null
                ), null, null, null
            );
            play_button.setText(R.string.start)
            play_button.setBackgroundResource(R.color.colorGreen)
        }
    }

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                RECORD_REQUEST_CODE
            )
        } else {
            initLocation()
        }
    }


    private fun initLocation() {
        initMap()
        val lm: LocationManager = getSystemService(
            Context.LOCATION_SERVICE
        ) as LocationManager
        try {
            lm.addTestProvider(
                testProvider, false, false, false, false, false,
                true, true, 0, 5
            )
            lm.removeTestProvider(testProvider)
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        } catch (e: SecurityException) {

            AlertDialog.Builder(this)
                .setTitle(R.string.system_settings_title)
                .setMessage(R.string.system_settings_message)
                .setPositiveButton(
                    R.string.system_setting_ok
                ) { dialog, which ->
                    startActivity(Intent(android.provider.Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS));
                }
                .setNegativeButton(R.string.system_setting_cancel, { d, w -> })
                .show()
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            RECORD_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    AlertDialog.Builder(this)
                        .setMessage(R.string.permission_setting_message)
                        .setPositiveButton(R.string.permission_setting_ok) { a, b ->
                            val intent =
                                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            val uri: Uri = Uri.fromParts("package", packageName, null)
                            intent.data = uri
                            startActivity(intent)
                        }
                        .setNegativeButton(R.string.permission_setting_cancel, { a, b -> })
                        .show()

                } else {
                    initLocation()
                }
            }
        }
    }


    fun initMap() {
        val mapFragment = fragmentManager.findFragmentById(R.id.mapView) as MapFragment
        mapFragment.getMapAsync { map ->
            huaweiMap = map
            map.isMyLocationEnabled = true
            map.uiSettings.isMyLocationButtonEnabled = true
            map.uiSettings.isCompassEnabled = true
            map.uiSettings.isRotateGesturesEnabled = true
            map.uiSettings.isScrollGesturesEnabled = true
            map.uiSettings.isScrollGesturesEnabledDuringRotateOrZoom = true
            map.uiSettings.isTiltGesturesEnabled = true
            map.uiSettings.isZoomControlsEnabled = true
            map.uiSettings.isIndoorLevelPickerEnabled = true
            map.uiSettings.isMapToolbarEnabled = true
            map.uiSettings.isZoomGesturesEnabled = false
            map.uiSettings.setAllGesturesEnabled(true)

            map.setOnMapLoadedCallback {
                Log.d("CC4", "map loaded")
            }
//            fusedLocationClient.lastLocation
//                .addOnSuccessListener { location: Location? ->
//                    if (location != null) {
//                        val params = CameraUpdateParam()
//                        params.latLng = LatLng(location.latitude, location.longitude)
//                        huaweiMap.animateCamera(CameraUpdate(params))
//                        //mMapView.controller.animateTo(GeoPoint(location.latitude, location.longitude))
//                    }
//                }

            huaweiMap.setOnCameraIdleListener {
                if (mocking) {
                    startSevice()
                }
            }
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }


}
