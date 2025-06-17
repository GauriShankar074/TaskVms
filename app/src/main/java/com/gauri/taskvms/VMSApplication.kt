package com.gauri.taskvms

import android.app.Application
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.gauri.taskvms.work.SyncUsers
import java.util.concurrent.TimeUnit

class VMSApplication:MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresCharging(true)
            .build()

        // Create a PeriodicWorkRequest for SyncCoroutineWorker
        val syncWorkRequest = PeriodicWorkRequestBuilder<SyncUsers>(15, TimeUnit.MINUTES)
        .setConstraints(constraints)
            .build()

        // Enqueue the periodic work
        WorkManager.getInstance(this).enqueue(syncWorkRequest)
    }
}