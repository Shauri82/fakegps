package com.shauri.fakegps

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.os.SystemClock
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.huawei.hms.location.FusedLocationProviderClient
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import java.util.concurrent.TimeUnit


class GpsService: Service() {
    private val NOTIFICATION_CHANNEL_ID = "main_gps_service"
    private val NOTIFICATION_ID = 766234
    private var disposable: Disposable? = null;
    val interval: Observable<Long> = Observable.interval(1, TimeUnit.SECONDS)
    var locationProvider: FusedLocationProviderClient? = null;

    var lat:Double?=null
    var lon:Double?=null

    override fun onBind(intent: Intent?): IBinder? =null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("CCC","on start command")
        lat = intent?.getDoubleExtra("lat",0.0)
        lon = intent?.getDoubleExtra("lon",0.0)
        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        locationProvider = FusedLocationProviderClient(applicationContext)
        startNotification()
        startMocking()
    }

    fun startNotification(){
        createChannel()
        val snoozeIntent =  Intent(STOP_MOCKING_ACTION);
        val snoozePendingIntent: PendingIntent =
            PendingIntent.getBroadcast(this, 0, snoozeIntent, 0)
        var builder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_play_circle_outline_24)
            .setContentTitle("Mocking location....")
            .setAutoCancel(false)
            .setOngoing(true)
            .addAction(R.drawable.btn_moreinfo,"Stop",snoozePendingIntent)
            .setContentIntent(bringToForegroundIntent())
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        with(NotificationManagerCompat.from(this)) {
            notify(NOTIFICATION_ID, builder.build())
        }
    }

    override fun onDestroy() {
        with(NotificationManagerCompat.from(this)) {
            this.cancelAll()
        }
        disposable?.dispose()
        locationProvider?.setMockMode(false)
        super.onDestroy()
    }

    private fun createChannel() {

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channelName: CharSequence ="asdasd"
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

        return  PendingIntent.getActivity(
            this, 0,
            notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)

    }

    private fun startMocking() {


        locationProvider?.setMockMode(true)
        val providerName = "shauriFakeGps"
        val loc = Location(providerName)
        val mockLocation = Location(providerName) // a string


        mockLocation.altitude = loc.altitude
        mockLocation.accuracy = 1f

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mockLocation.bearingAccuracyDegrees = 0.1f
            mockLocation.verticalAccuracyMeters = 0.1f
            mockLocation.speedAccuracyMetersPerSecond = 0.01f
        }



        disposable = interval.subscribe {
            Log.d("CCC", "mocking ..." + lat + " " + lon);
            mockLocation.latitude = lat ?: 0.0
            mockLocation.longitude = lon ?: 0.0
            mockLocation.time = System.currentTimeMillis()
            mockLocation.elapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos()
            locationProvider?.setMockLocation(mockLocation)
        }
    }



}