package com.gauri.taskvms.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.gauri.taskvms.db.AppDatabase
import com.gauri.taskvms.network.ApiInterface
import com.gauri.taskvms.network.ConnectivityManager
import com.gauri.taskvms.network.RetrofitServices
import com.gauri.taskvms.network.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SyncUsers(private val appContext: Context, workerParams: WorkerParameters) :CoroutineWorker(appContext,workerParams){
    val isConnected = ConnectivityManager().isConnected
    var retrofitServices: ApiInterface
    init {
        retrofitServices = RetrofitServices.getInstance(appContext).create(ApiInterface::class.java)
    }
    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO){
            val users = AppDatabase.getDatabase(appContext).getUserDao().getAllUser()
            users.forEach {
                if(isConnected.value){
                    val updateUser = User(it.currentCompany,it.dob,it.email,it.firstName,it.isWorking,it.lastName)
                    val res = retrofitServices.saveUser(updateUser)
                    if(res.isSuccessful){
                        AppDatabase.getDatabase(appContext).getUserDao().updateStatus(res.body()?.status?:false,it.id)
                    }
                }
            }
            return@withContext Result.success()
        }
    }

}