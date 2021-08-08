package com.devlogs.client_android.broadcastreceiver

import android.content.*
import android.os.IBinder
import android.telephony.SmsMessage
import android.util.Log
import com.devlogs.client_android.androidservice.ReadMessageService
import java.lang.Exception


class SmsReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val bundle = intent!!.extras
        try {
            if (bundle != null) {
                val pdusObj = bundle["pdus"] as Array<Any>?
                for (i in pdusObj!!.indices) {
                    val currentMessage: SmsMessage =
                        SmsMessage.createFromPdu(pdusObj[i] as ByteArray)
                    val phoneNumber: String = currentMessage.getDisplayOriginatingAddress()
                    val message: String = currentMessage.getDisplayMessageBody()
                    val createdDate : Long = currentMessage.timestampMillis
                    Log.d("SmsReceiver", "senderNum: $phoneNumber; message: $message")
                    val service = (peekService(context!!, Intent(context, ReadMessageService::class.java)) as ReadMessageService.LocalBinder).service
                    service.sendSms(phoneNumber, message, createdDate)
                }
            }
        } catch (e: Exception) {
            Log.e("SmsReceiver", "Exception smsReceiver$e")
        }
    }
}