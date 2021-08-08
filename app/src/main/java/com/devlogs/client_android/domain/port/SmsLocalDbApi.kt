package com.devlogs.client_android.domain.port

import com.devlogs.client_android.data.offline.PaymentSmsRealmObject

interface SmsLocalDbApi {
    suspend fun saveMessage (paymentSms: PaymentSmsRealmObject)
    suspend fun getAll () : List<PaymentSmsRealmObject>
    suspend fun clear ()
}