package com.shauri.fakegps.ui.main

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.huawei.hms.location.FusedLocationProviderClient
import com.huawei.hms.location.LocationServices
import com.huawei.hms.maps.CameraUpdate
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.MapFragment
import com.huawei.hms.maps.model.CameraUpdateParam
import com.huawei.hms.maps.model.LatLng
import com.shauri.fakegps.*
import com.shauri.fakegps.dependency.AppComponent
import com.shauri.fakegps.ui.base.BaseActivity
import com.shauri.fakegps.ui.router.Router
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_footer_main.*

class MainActivity : BaseActivity<MainPresenter>(), MainUi,
    NavigationView.OnNavigationItemSelectedListener {
    override fun provideLayoutRes() = R.layout.activity_main

    override fun providePresenter(router: Router, component: AppComponent?) =
        MainPresenter(this, router, component)

    private val testProvider = "gpstest"
    private val RECORD_REQUEST_CODE = 101
    private var mocking = false
    private val firebase = FirebaseConfig()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var huaweiMap: HuaweiMap
    private val compositeDisposable = CompositeDisposable()

    val receiver = FakeGpsBroadcastReceiver(Runnable {
        mocking = false
        handleMocking()
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val filter = IntentFilter()
        filter.addAction(STOP_MOCKING_ACTION)
        //this.registerReceiver(receiver, filter)
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
        val v = getString(R.string.version_name)
        tvVersion.setText(String.format(v, BuildConfig.VERSION_NAME))
        nav_view.setNavigationItemSelectedListener(this)
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
            pin.visibility = View.GONE
            presenter.startMocking(huaweiMap.cameraPosition.target)
            Log.d("CCC", "mocking stared")
        } else {
            pin.visibility = View.VISIBLE
            presenter.stopService()
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
        compositeDisposable.add(Completable.mergeArray(initMap(), initFusedLocation()).subscribe({
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        val params = CameraUpdateParam()
                        params.newLatLngZoom = CameraUpdateParam.NewLatLngZoom(
                            LatLng(
                                location.latitude,
                                location.longitude
                            ), 12.0f
                        )

                        huaweiMap.animateCamera(CameraUpdate(params))
                    }
                }
        }, { }))

    }

    fun initFusedLocation(): Completable {
        return Completable.create { emmiter ->
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
                emmiter.onComplete()

            } catch (e: SecurityException) {
                emmiter.onError(Exception())
                AlertDialog.Builder(this)
                    .setTitle(R.string.system_settings_title)
                    .setMessage(R.string.system_settings_message)
                    .setPositiveButton(
                        R.string.system_setting_ok
                    ) { dialog, which ->
                        startActivity(Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS));
                    }
                    .setNegativeButton(R.string.system_setting_cancel, { d, w -> })
                    .show()
            }
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


    fun initMap(): Completable {
        return Completable.create { emmiter ->

            val mapFragment = fragmentManager.findFragmentById(R.id.mapView) as MapFragment
            mapFragment.getMapAsync { map ->
                huaweiMap = map
                map.isMyLocationEnabled = true
                map.uiSettings.isMyLocationButtonEnabled = true
                map.uiSettings.isScrollGesturesEnabled = true
                map.uiSettings.isScrollGesturesEnabledDuringRotateOrZoom = true
                map.uiSettings.isTiltGesturesEnabled = true
                map.uiSettings.isZoomControlsEnabled = true
                map.uiSettings.isIndoorLevelPickerEnabled = true
                map.uiSettings.isMapToolbarEnabled = true
                map.uiSettings.isZoomGesturesEnabled = false
                map.uiSettings.setAllGesturesEnabled(true)

                map.setOnMapLoadedCallback {
                    emmiter.onComplete()
                }
            }
        }


    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        presenter.onItemSelected(item.itemId)

        return true;
    }

    override fun closeMenu() {
        drawerLayout.close()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
        compositeDisposable.dispose()
    }
}