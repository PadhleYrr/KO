package com.ramacademy.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RamAcademyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nm = getSystemService(NotificationManager::class.java)

            nm.createNotificationChannel(
                NotificationChannel(
                    CHANNEL_GENERAL, "General", NotificationManager.IMPORTANCE_DEFAULT
                ).apply { description = "General app notifications" }
            )
            nm.createNotificationChannel(
                NotificationChannel(
                    CHANNEL_LIVE, "Live Classes", NotificationManager.IMPORTANCE_HIGH
                ).apply { description = "Alerts for upcoming live classes" }
            )
            nm.createNotificationChannel(
                NotificationChannel(
                    CHANNEL_DOWNLOAD, "Downloads", NotificationManager.IMPORTANCE_LOW
                ).apply { description = "File download progress" }
            )
        }
    }

    companion object {
        const val CHANNEL_GENERAL  = "ram_general"
        const val CHANNEL_LIVE     = "ram_live"
        const val CHANNEL_DOWNLOAD = "ram_download"
    }
}
