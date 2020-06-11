package com.shauri.fakegps

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Rect
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.huawei.hms.location.FusedLocationProviderClient
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.*
import org.osmdroid.api.IGeoPoint
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import java.io.File
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    lateinit var mMockLocation: Location;
    lateinit var mLocationManager: LocationManager
    private val RECORD_REQUEST_CODE = 101

    private var mocking=false
    private var disposable:Disposable?=null;
    val interval: Observable<Long> = Observable.interval(1, TimeUnit.SECONDS)
    var locationProvider:FusedLocationProviderClient?=null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupPermissions();

        play_button.setOnClickListener {
            mocking = !mocking
            if(mocking){
                play_button.setBackgroundResource(R.drawable.ic_baseline_pause_circle_outline_24)
                startMocking()
                Log.d("CCC","mocking stared")
            }
            else{
                disposable?.dispose()
                locationProvider?.setMockMode(false)
                Log.d("CCC","mocking stoped")
                play_button.setBackgroundResource(R.drawable.ic_baseline_play_circle_outline_24)
            }
        }
        initMap(mapView)
    }

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_FINE_LOCATION)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                RECORD_REQUEST_CODE)
        }
        else{
            init();
        }
    }


    private fun init(){
        locationProvider = FusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
    }

    private fun startMocking(){



        locationProvider?.setMockMode(true)
        val providerName ="shauriFakeGps"
        val loc = Location(providerName)
        val mockLocation = Location(providerName) // a string


        mockLocation.altitude = loc.altitude
        mockLocation.accuracy = 1f

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mockLocation.bearingAccuracyDegrees = 0.1f
            mockLocation.verticalAccuracyMeters = 0.1f
            mockLocation.speedAccuracyMetersPerSecond = 0.01f
        }
//        locationManager.setTestProviderLocation(providerName, mockLocation)
       // locationProvider.setMockLocation(mockLocation)
//        mLocationManager =
//            getSystemService(Context.LOCATION_SERVICE) as LocationManager;
//        if (mLocationManager.getProvider(providerName) != null) {
//            mLocationManager.removeTestProvider(providerName);
//        }
//
//
//        mLocationManager.addTestProvider(providerName, true, false, false, false, true, true, true,
//            Criteria.POWER_LOW, Criteria.ACCURACY_FINE);
//        mLocationManager.setTestProviderEnabled(providerName, true);



        disposable=interval.subscribe {
            Log.d("CCC","mocking ...");
            val geoPoint = getPoint()
            mockLocation.latitude = geoPoint.latitude
            mockLocation.longitude = geoPoint.longitude
            mockLocation.time = System.currentTimeMillis()
            mockLocation.elapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos()
            locationProvider?.setMockLocation(mockLocation)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            RECORD_REQUEST_CODE -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {


                } else {
                    init();
                }
            }
        }
    }


    fun initMap(mMapView: MapView) {
        val basePath = File(cacheDir.absolutePath, "osmdroid")
        Configuration.getInstance().osmdroidBasePath = basePath
        val tileCache = File(Configuration.getInstance().osmdroidBasePath.absolutePath, "tile")
        Configuration.getInstance().osmdroidTileCache = tileCache
        Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID
        mMapView.setUseDataConnection(true)
        mMapView.setTileSource(TileSourceFactory.MAPNIK)
        mMapView.isClickable = true
        mMapView.setBuiltInZoomControls(false)
        mMapView.isTilesScaledToDpi = true;
        mMapView.setMultiTouchControls(true)
        mMapView.isTilesScaledToDpi = true
        mMapView.overlays.clear()
        mMapView.controller.setZoom(16.0)
        mMapView.controller.animateTo(GeoPoint(52.156930, 21.065316))
    }

    fun getPoint():IGeoPoint{
        val r: Rect = mapView.projection.getScreenRect()
        val y: Int = pin.getBottom()
        val x = r.right / 2
        return mapView.projection.fromPixels(x, y)
    }

    override fun onDestroy() {
        super.onDestroy()
        locationProvider?.setMockMode(false)
    }
}
