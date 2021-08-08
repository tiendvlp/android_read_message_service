package com.devlogs.client_android.screen

import android.Manifest
import android.annotation.SuppressLint
import android.content.*
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.widget.Button
import android.widget.ScrollView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.devlogs.client_android.R
import com.devlogs.client_android.androidservice.ReadMessageService
import com.devlogs.client_android.common.coroutine.BackgroundDispatcher
import com.devlogs.client_android.domain.entity.PaymentSmsEntity
import com.devlogs.client_android.domain.port.PaymentApi
import com.devlogs.client_android.sendpaymentsms.SendSmsPaymentUseCaseSync
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.NonCancellable.start
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.RuntimeException
import java.util.*
import javax.inject.Inject


private const val SMS_REQUEST_CODE = 101

@SuppressLint("SetTextI18n")
@AndroidEntryPoint
class MainActivity : AppCompatActivity(), ServiceConnection {
    private lateinit var btnStart: Button
    private lateinit var btnStop: Button
    private lateinit var txtServiceStatus: TextView
    private val sb: StringBuilder = StringBuilder()
    private lateinit var serviceIntent: Intent
    private val handler = Handler()
    private var service: ReadMessageService? = null

    @Inject
    lateinit var smsPaymentUseCaseSync: SendSmsPaymentUseCaseSync
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_SMS),
            SMS_REQUEST_CODE
        )
        addControls()
        addEvents()
        serviceIntent = Intent(this, ReadMessageService::class.java)
        txtServiceStatus.text = "Connecting to service"
        ContextCompat.startForegroundService(this, serviceIntent)
    }

    private fun addControls() {
        btnStart = findViewById(R.id.btnStartService)
        btnStop = findViewById(R.id.btnStopService)
        txtServiceStatus = findViewById(R.id.txtStatus)

    }

    private fun addEvents() {
        btnStart.setOnClickListener {
            if (service != null && !service!!.isStarted) {
                service?.start()
                txtServiceStatus.text = "Connected, service is running"
            }

        }

        btnStop.setOnClickListener {
            if (service != null && service!!.isStarted) {
                service?.stop()
                txtServiceStatus.text = "Connected, service is not running"
            }
        }
    }

    override fun onStart() {
        super.onStart()
        bindService(serviceIntent, this, Context.BIND_AUTO_CREATE)
    }

    override fun onStop() {
        unbindService(this)
        super.onStop()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (permissions.size == 0 || grantResults.size == 0) {
            // request permission has been canceled
            return
        }

        when (requestCode) {
            SMS_REQUEST_CODE -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted
                } else if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        permissions[0]
                    )
                ) {
                    // permission denied
                } else {
                    // permission denied and user don't want to ask for it again
                }
            }
            else -> {
                throw RuntimeException("UnHandle permission request code: $requestCode")
            }
        }
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        this.service = (service as ReadMessageService.LocalBinder).service
        Log.d(ReadMessageService.TAG, "onServiceConnected")
        if (this.service!!.isStarted) {
            txtServiceStatus.text = "Connected, service is running"
        } else {
            txtServiceStatus.text = "Connected, service is not running"
        }
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        Log.d(ReadMessageService.TAG, "onServiceDisconnected")
        this.service = null
        txtServiceStatus.text = "Disconnected, running state: unknown"
    }
}