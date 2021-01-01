package com.shauri.fakegps

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.IBinder
import android.os.SystemClock
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.huawei.hms.location.FusedLocationProviderClient
import com.shauri.fakegps.data.ServiceData
import com.shauri.fakegps.ui.main.MainActivity
import com.shauri.fakegps.ui.router.SERVICE_DATA
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import timber.log.Timber
import java.util.concurrent.TimeUnit
import kotlin.random.Random


class GpsService : Service() {
    private val NOTIFICATION_CHANNEL_ID = "main_gps_service"
    private val NOTIFICATION_ID = 766234
    private var disposable: Disposable? = null;

    var locationProviderHms: FusedLocationProviderClient? = null
    var locationProviderGms: com.google.android.gms.location.FusedLocationProviderClient? = null

    var lat: Double? = null
    var lon: Double? = null
    lateinit var data: ServiceData


    override fun onBind(intent: Intent?): IBinder? = null

    @SuppressLint("MissingPermission")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.d("on start command")
        data = intent?.getParcelableExtra(SERVICE_DATA) as ServiceData
        lat = data.point?.latitude ?: 0.0
        lon = data.point?.longitude ?: 0.0
        if (canMock()) {
            startMocking()
        }
        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        locationProviderHms = FusedLocationProviderClient(applicationContext)
        locationProviderGms = com.google.android.gms.location.FusedLocationProviderClient(
            applicationContext
        )
        startNotification()

    }

    fun startNotification() {
        createChannel()
        val snoozeIntent = Intent(STOP_MOCKING_ACTION);
        val snoozePendingIntent: PendingIntent =
            PendingIntent.getBroadcast(this, 0, snoozeIntent, 0)
        var builder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_play_circle_outline_24)
            .setContentTitle("Mocking location....")
            .setAutoCancel(false)
            .setOngoing(true)
            .addAction(R.drawable.ic_baseline_menu_24, "Stop", snoozePendingIntent)
            .setContentIntent(bringToForegroundIntent())
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        startForeground(NOTIFICATION_ID, builder.build())

    }

    override fun onDestroy() {
        with(NotificationManagerCompat.from(this)) {
            this.cancelAll()
        }
        disposable?.dispose()
        stopMocking()
        super.onDestroy()
    }

    @SuppressLint("MissingPermission")
    fun stopMocking() {
        if (data.hms) {
            locationProviderHms?.setMockMode(false)
        }
        if (data.gms) {
            if (canMock()) {
                locationProviderGms?.setMockMode(false)
            }

        }
    }

    fun canMock(): Boolean {
        return (data.gms || data.hms) && (
                ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED)

    }

    private fun createChannel() {

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelName: CharSequence = "Main"
        val notificationChannel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            channelName,
            NotificationManager.IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(notificationChannel)

    }

    private fun bringToForegroundIntent(): PendingIntent? {
        val notificationIntent = Intent(this, MainActivity::class.java)
        notificationIntent.action = Intent.ACTION_MAIN
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        return PendingIntent.getActivity(
            this, 0,
            notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )

    }

    @RequiresPermission(anyOf = ["android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"])
    private fun startMocking() {


        if (data.hms) {
            locationProviderHms?.setMockMode(true)
        }

        if (data.gms) {
            locationProviderGms?.setMockMode(true)
        }
        val providerName = "shauriFakeGps"
        val loc = Location(providerName)
        val mockLocation = Location(providerName)

        mockLocation.altitude = loc.altitude
        mockLocation.accuracy = data.accuracy.toFloat()
        mockLocation.bearingAccuracyDegrees = 0.1f
        mockLocation.verticalAccuracyMeters = 0.1f
        mockLocation.speedAccuracyMetersPerSecond = 0.01f

        disposable = Observable.interval(data.interval.toLong(), TimeUnit.SECONDS).subscribe {

            lat = lat?.plus(getDeltaLat())
            lon = lon?.plus(getDeltaLon())
            mockLocation.latitude = lat ?: 0.0
            mockLocation.longitude = lon ?: 0.0
            mockLocation.time = System.currentTimeMillis()
            mockLocation.elapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos()
            if (data.hms) {
                locationProviderHms?.setMockLocation(mockLocation)
            }
            if (data.gms) {
                locationProviderGms?.setMockLocation(mockLocation)
            }
        }

    }

    private fun getDeltaLat(): Double {
        if (data.mockMove) {
            if (data.randomMove) {
                return Random.nextInt(-10, 10) / 10000.0;
            }
            if (data.direction != null) {
                val delta = -0.0005 * Math.sin(-Math.toRadians(data.direction!!))
                return delta
            }
        }
        return 0.0
    }

    private fun getDeltaLon(): Double {
        if (data.mockMove) {
            if (data.randomMove) {
                return Random.nextInt(-10, 10) / 10000.0;
            }
            if (data.direction != null) {
                val delta = 0.0005 * Math.cos(-Math.toRadians(data.direction!!))
                return delta;
            }
        }
        return 0.0
    }

}