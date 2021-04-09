package com.studio.king.demomovie

import android.app.Activity
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.multidex.MultiDex
import com.gu.toolargetool.TooLargeTool
import com.studio.king.demomovie.di.appModules
import io.easyprefs.Prefs
import leakcanary.AppWatcher
import leakcanary.ObjectWatcher
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

open class MovieApplication: Application(), Application.ActivityLifecycleCallbacks{
    companion object {
        lateinit var sInstance : MovieApplication
        fun getRefWatcher(context: Context): ObjectWatcher? {
            val application : MovieApplication = context.applicationContext as MovieApplication
            return application.refWatcher
        }
    }

    private var refWatcher: ObjectWatcher? = null



    override fun onCreate() {
        super.onCreate()
        sInstance = this@MovieApplication

        refWatcher = AppWatcher.objectWatcher

        TooLargeTool.startLogging(this)

        Prefs.initializeApp(this)

        registerActivityLifecycleCallbacks(this)


        startKoin {
            androidLogger()
            androidContext(this@MovieApplication)
            modules(appModules)
        }

        createNotificationChannel()


    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            val channelId = getString(R.string.default_notification_channel_id)
            val channelName = getString(R.string.key_default_notification_channel_name)
            val mChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
            mChannel.enableVibration(true)
            mChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(mChannel)
        }
    }


    //listener Application.ActivityLifecycleCallbacks

    var currentActivity: Activity? = null

    /** ActivityLifecycleCallback methods  */
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
    override fun onActivityStarted(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityResumed(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityStopped(activity: Activity) {}
    override fun onActivityPaused(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {
        currentActivity = null
    }
}