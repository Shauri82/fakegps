package com.shauri.fakegps.ui.main

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.huawei.hms.location.FusedLocationProviderClient
import com.huawei.hms.location.LocationServices
import com.huawei.hms.maps.CameraUpdate
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.MapFragment
import com.huawei.hms.maps.model.CameraUpdateParam
import com.huawei.hms.maps.model.LatLng
import com.shauri.fakegps.FakeGpsBroadcastReceiver
import com.shauri.fakegps.R
import com.shauri.fakegps.STOP_MOCKING_ACTION
import com.shauri.fakegps.dependency.AppComponent
import com.shauri.fakegps.ui.base.BaseActivity
import com.shauri.fakegps.ui.dialog.InputStringDialog
import com.shauri.fakegps.ui.getColorByAttributeId
import com.shauri.fakegps.ui.router.KEY_LOCATION
import com.shauri.fakegps.ui.router.REQUEST_LOCATION
import com.shauri.fakegps.ui.router.Router
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import java.util.*


class MainActivity : BaseActivity<MainPresenter>(), MainUi,
    NavigationView.OnNavigationItemSelectedListener {
    override fun provideLayoutRes() = R.layout.activity_main

    override fun providePresenter(router: Router, component: AppComponent?) =
        MainPresenter(this, router, component)

    private val testProvider = "gpstest"
    private val RECORD_REQUEST_CODE = 101


    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var huaweiMap: HuaweiMap? = null
    private val compositeDisposable = CompositeDisposable()

    private val receiver = FakeGpsBroadcastReceiver {
        presenter.setMockingStoped()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val filter = IntentFilter()
        filter.addAction(STOP_MOCKING_ACTION)
        this.registerReceiver(receiver, filter)
        presenter.checkVersion()
        play_button.setOnClickListener {
            presenter.onButtonClicked(huaweiMap?.cameraPosition?.target)
        }

        setSupportActionBar(toolbar)

        toolbar.setNavigationIcon(R.drawable.ic_baseline_menu_24)
        toolbar.setNavigationOnClickListener { v: View? ->
            drawerLayout.openDrawer(GravityCompat.START)
        }

        nav_view.setNavigationItemSelectedListener(this)
        activityMain_btnSaveLocation.setOnClickListener { presenter.onSaveLocationClicked(huaweiMap?.cameraPosition?.target) }

    }


    override fun showUpgradeDialog() {
        AlertDialog.Builder(this)
            .setCancelable(false)
            .setMessage(R.string.version)
            .setPositiveButton(
                R.string.close
            ) { _, _ ->
                presenter.onGoToAppGalleryClicked()
            }
            .show()
    }

    override fun showTooMuchLocations(max: Int) {
        val text = getString(R.string.activityMain_save_location_too_many)
        AlertDialog.Builder(this)
            .setMessage(String.format(text, max))
            .setPositiveButton(
                R.string.ok
            ) { _, _ ->
            }
            .show()
    }

    override fun setPinVisibility(visibility: Int) {
        pin.visibility = visibility
    }

    override fun setButtonText(label: Int) {
        play_button.setText(label)
    }

    override fun setButtonBackground(background: Int) {
        play_button.setBackgroundColor(getColorByAttributeId(background))
    }

    override fun setPauseButton() {
        play_button.setCompoundDrawablesRelativeWithIntrinsicBounds(
            ResourcesCompat.getDrawable(
                getResources(),
                R.drawable.ic_baseline_pause_24,
                null
            ), null, null, null
        );
    }

    override fun setPlayButton() {
        play_button.setCompoundDrawablesRelativeWithIntrinsicBounds(
            ResourcesCompat.getDrawable(
                getResources(),
                R.drawable.ic_baseline_play_arrow_24,
                null
            ), null, null, null
        );
    }

    override fun setupPermissions() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {

            val permissions = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            checkPermissions(permissions)

        } else {
            val permissions = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                "android.permission.ACCESS_BACKGROUND_LOCATION"
            )
            checkPermissions(permissions)
        }

    }

    private fun checkPermissions(permissions: Array<String>) {
        if (Arrays.stream(permissions).anyMatch {
                ActivityCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
            }) {
            ActivityCompat.requestPermissions(this, permissions, RECORD_REQUEST_CODE)
        } else {
            initLocation()
        }
    }


    private fun initLocation() {
        compositeDisposable.add(
            Completable.mergeArray(initMap(), initFusedLocation()).subscribe(
                {
                    fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
                    fusedLocationClient?.lastLocation
                        ?.addOnSuccessListener { location: Location? ->
                            goToLocation(location?.latitude, location?.longitude)
                        }
                        ?.addOnFailureListener { Timber.e(it) }
                },
                { t ->
                    Timber.e(t)
                })
        )

    }

    override fun goToLocation(lat: Double?, lon: Double?) {
        if (lat != null && lon != null) {
            val params = CameraUpdateParam()
            params.newLatLngZoom = CameraUpdateParam.NewLatLngZoom(
                LatLng(
                    lat,
                    lon
                ), 12.0f
            )
            huaweiMap?.animateCamera(CameraUpdate(params))
        }
    }

    private fun initFusedLocation(): Completable {
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

                emmiter.onComplete()

            } catch (e: SecurityException) {
                AlertDialog.Builder(this)
                    .setTitle(R.string.system_settings_title)
                    .setMessage(R.string.system_settings_message)
                    .setPositiveButton(
                        R.string.system_setting_ok
                    ) { dialog, which ->
                        startActivity(Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS));
                    }
                    .setNegativeButton(R.string.system_setting_cancel) { _, _ -> }
                    .show()
                emmiter.onComplete()
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
                        .setPositiveButton(R.string.permission_setting_ok) { _, _ ->
                            val intent =
                                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            val uri: Uri = Uri.fromParts("package", packageName, null)
                            intent.data = uri
                            startActivity(intent)
                        }
                        .setNegativeButton(R.string.permission_setting_cancel) { _, _ -> }
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

                map.setOnMapLoadedCallback {
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
                    emmiter.onComplete()
                }
            }
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_LOCATION -> {
                val position = data?.getParcelableExtra<LatLng>(KEY_LOCATION)
                if (position != null) {
                    presenter.setMockingStoped()
                    goToLocation(position.latitude, position.longitude)
                }
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
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

    override fun showDialog(listener: InputStringDialog.OnSaveClickedListener) {
        InputStringDialog(this, listener).show()
    }

    override fun showSaveError(@StringRes error: Int) {
        val snackbar: Snackbar =
            Snackbar.make(findViewById(android.R.id.content), error, Snackbar.LENGTH_LONG)
        snackbar.view.setBackgroundColor(ContextCompat.getColor(this, R.color.colorRed))
        snackbar.show()
    }

    override fun showToast(label: Int) {
        Toast.makeText(this, label, Toast.LENGTH_LONG).show()
    }
}