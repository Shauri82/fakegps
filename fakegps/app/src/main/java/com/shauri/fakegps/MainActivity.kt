package com.shauri.fakegps

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Rect
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.huawei.hms.location.FusedLocationProviderClient
import com.huawei.hms.location.LocationServices
import kotlinx.android.synthetic.main.activity_main.*
import org.osmdroid.api.IGeoPoint
import org.osmdroid.config.Configuration
import org.osmdroid.config.Configuration.*
import org.osmdroid.events.DelayedMapListener
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import java.io.File

class MainActivity : AppCompatActivity() {
    private val testProvider = "gpstest"
    private val RECORD_REQUEST_CODE = 101
    private var mocking = false
    private lateinit var fusedLocationClient: FusedLocationProviderClient

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
        setupPermissions();
        play_button.setOnClickListener {
            mocking = !mocking
            handleMocking()
        }

        gps_button.setOnClickListener{

        }

    }

    private fun startSevice() {
        var intent = Intent(this, GpsService::class.java)
        val point = getPoint()
        intent.putExtra("lat", point.latitude)
        intent.putExtra("lon", point.longitude)
        startService(intent)
    }

    private fun handleMocking() {
        if (mocking) {
            play_button.setCompoundDrawablesRelativeWithIntrinsicBounds(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_baseline_pause_24,null),null,null,null);
            play_button.setText(R.string.stop)
            play_button.setBackgroundResource(R.color.colorRed)
            startSevice()
            Log.d("CCC", "mocking stared")
        } else {
            stopService(
                Intent(this, GpsService::class.java)
            )
            Log.d("CCC", "mocking stoped")
            play_button.setCompoundDrawablesRelativeWithIntrinsicBounds(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_baseline_play_arrow_24,null),null,null,null);
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
            initMap(mapView)
        } catch (e: SecurityException) {

            AlertDialog.Builder(this)
                .setTitle(R.string.system_settings_title)
                .setMessage(R.string.system_settings_message)
                .setPositiveButton(
                    R.string.system_setting_ok,
                    DialogInterface.OnClickListener { dialog, which ->
                        startActivity(Intent(android.provider.Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS));
                    })
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
                        .setNegativeButton(R.string.permission_setting_cancel,{a,b->})
                        .show()

                } else {
                    initLocation()
                }
            }
        }
    }


    fun initMap(mMapView: MapView) {
        getInstance().userAgentValue = BuildConfig.APPLICATION_ID
        getInstance().osmdroidBasePath = File(this.getFilesDir().absolutePath + "/osmdroid/base");
        getInstance().osmdroidTileCache = File(this.getFilesDir().absolutePath + "/osmdroid/cache");
        mMapView.setUseDataConnection(true)
        mMapView.setTileSource(TileSourceFactory.MAPNIK)
        mMapView.isClickable = true
        mMapView.setBuiltInZoomControls(false)
        mMapView.isTilesScaledToDpi = true;
        mMapView.setMultiTouchControls(true)
        mMapView.isTilesScaledToDpi = true
        mMapView.overlays.clear()
        mMapView.controller.setZoom(16.0)

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    mMapView.controller.animateTo(GeoPoint(location.latitude, location.longitude))
                }
            }
        mMapView.addMapListener(DelayedMapListener(object : MapListener {
            override fun onScroll(scrollEvent: ScrollEvent): Boolean {
                if (mocking) {
                    startSevice()
                }
                return true
            }

            override fun onZoom(zoomEvent: ZoomEvent): Boolean {

                return true
            }
        }, 1000))
    }

    fun getPoint(): IGeoPoint {
        val r: Rect = mapView.projection.getScreenRect()
        val y: Int = pin.getBottom()
        val x = r.right / 2
        return mapView.projection.fromPixels(x, y)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }


}
