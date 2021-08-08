package com.devlogs.client_android.androidservice

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.app.NotificationManager
import android.app.NotificationChannel
import android.content.Context
import android.content.ServiceConnection
import android.os.Binder
import android.os.Build
import android.util.Log
import com.devlogs.client_android.common.coroutine.BackgroundDispatcher
import com.devlogs.client_android.domain.entity.PaymentSmsEntity
import com.devlogs.client_android.screen.MainActivity
import com.devlogs.client_android.sendpaymentsms.SendSmsPaymentUseCaseSync
import dagger.hilt.EntryPoints
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class ReadMessageService : Service() {
    companion object {
        val TAG = "ReadMessageService"
        fun bind (context: Context, connection: ServiceConnection, flag: Int = Context.BIND_AUTO_CREATE) {
            val intent = Intent(context, ReadMessageService::class.java)
            context.bindService(intent, connection, flag)
        }
    }

    inner class LocalBinder : Binder() {
        val service = this@ReadMessageService
    }
    @Inject
    lateinit var retrofit: Retrofit
    @Inject
    lateinit var sendSmsPaymentUseCase: SendSmsPaymentUseCaseSync

    private val SYNC_MESSAGE_NOTIFICATION_CHANNEL = "SYNCMESSAGE"
    private lateinit var binder: LocalBinder
    private val coroutine = CoroutineScope(BackgroundDispatcher)
    var isStarted = false; private set

    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
        super.onDestroy()
    }

    fun sendSms (address: String, content: String, createdDate: Long) {
        if (!isStarted) return
        Log.d("ReadMessageService", "sender: $address, sms: $content")
        coroutine.launch {
            val result = sendSmsPaymentUseCase.execute(PaymentSmsEntity(createdDate, content, address))
            if (result is SendSmsPaymentUseCaseSync.Result.Success) {
                Log.d(TAG, "Send sms success: (address: $address, content: $content)")
            } else if (result is SendSmsPaymentUseCaseSync.Result.NetworkError) {
                Log.d(TAG, "Send sms failed due to NetworkError: (address: $address, content: $content)")
            } else if (result is SendSmsPaymentUseCaseSync.Result.GeneralError) {
                Log.d(TAG, "Send sms failed due to GeneralError: ${result.errorMessage} ( address: $address, content: $content)")
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        binder = LocalBinder()
        createNotificationChannel()
        val pendingIntent: PendingIntent =
            Intent(this, MainActivity::class.java).let { notificationIntent ->
                PendingIntent.getActivity(this, 0, notificationIntent, 0)
            }
        val notification: Notification = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(this, SYNC_MESSAGE_NOTIFICATION_CHANNEL)
                .setContentTitle("Sync message service")
                .setContentIntent(pendingIntent)
                .build()
        } else {
            Notification.Builder(this)
                .setContentTitle("Sync message service")
                .setContentIntent(pendingIntent)
                .build()
        }

        startForeground(1, notification)

    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.d(TAG, "onBind")
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d(TAG, "onBind")
        return super.onUnbind(intent)
    }

    fun stop () {
        isStarted = false
    }

    fun start () {
        isStarted = true
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                SYNC_MESSAGE_NOTIFICATION_CHANNEL,
                "Sync message",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(
                NotificationManager::class.java
            )
            manager.createNotificationChannel(serviceChannel)
        }
    }

}