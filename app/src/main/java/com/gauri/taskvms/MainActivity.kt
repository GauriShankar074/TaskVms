package com.gauri.taskvms

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.gauri.taskvms.db.AppDatabase
import com.gauri.taskvms.db.UserEntity
import com.gauri.taskvms.network.ConnectivityManager
import com.gauri.taskvms.ui.theme.TaskVmsTheme
import com.gauri.taskvms.work.SyncUsers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TaskVmsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}



@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val isConnected = remember {  ConnectivityManager().isConnected}
    LaunchedEffect(isConnected) {
        if(isConnected.value){
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(true)
                .build()

            // Create a PeriodicWorkRequest for SyncCoroutineWorker
            val syncWorkRequest = OneTimeWorkRequestBuilder<SyncUsers>()
                .setConstraints(constraints)
                .build()

            // Enqueue the periodic work
            WorkManager.getInstance(context.applicationContext).enqueue(syncWorkRequest)
        }
    }
    Column(modifier.fillMaxSize()) {
        var firstName by rememberSaveable  {  mutableStateOf("")}
        var lastName by rememberSaveable  {  mutableStateOf("")}
        var email by rememberSaveable  {  mutableStateOf("")}
        var dob by rememberSaveable  {  mutableStateOf("")}
        var isWorking by rememberSaveable  {  mutableStateOf(false)}
        var companyName by rememberSaveable  {  mutableStateOf("")}
        TextField(firstName, onValueChange = {
            firstName = it
        }, placeholder = {
            Text("Enter first name")
        }, label = {
            Text("First Name")
        })
        TextField(lastName, onValueChange = {
            lastName = it
        }, placeholder = {
            Text("Enter last name")
        }, label = {
            Text("Last Name")
        })
        TextField(email, onValueChange = {
            email = it
        }, placeholder = {
            Text("Enter email")
        }, label = {
            Text("Email")
        })
        TextField(dob, onValueChange = {
            dob = it
        }, placeholder = {
            Text("Enter dob")
        }, label = {
            Text("DOB")
        })

        Row {
            Checkbox(isWorking, onCheckedChange = {
                isWorking = it
            })
            Text("Is Working")
        }


        TextField(companyName, onValueChange = {
            companyName = it
        }, placeholder = {
            Text("Enter company name")
        }, label = {
            Text("Company name")
        })
        Button(onClick = {
            val userEntity = UserEntity(firstName = firstName, lastName =  lastName, email = email, dob = dob, isWorking = isWorking, currentCompany = companyName)
            coroutineScope.launch(Dispatchers.IO) {
                AppDatabase.getDatabase(context.applicationContext).getUserDao().addUser(userEntity)
            }.invokeOnCompletion {
                val constraints = Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .setRequiresCharging(true)
                    .build()

                // Create a PeriodicWorkRequest for SyncCoroutineWorker
                val syncWorkRequest = OneTimeWorkRequestBuilder<SyncUsers>()
                    .setConstraints(constraints)
                    .build()

                // Enqueue the periodic work
                WorkManager.getInstance(context.applicationContext).enqueue(syncWorkRequest)
            }

        }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text("Save")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TaskVmsTheme {
        Greeting("Android")
    }
}