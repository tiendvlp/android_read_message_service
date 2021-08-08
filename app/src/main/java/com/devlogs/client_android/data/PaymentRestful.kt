package com.devlogs.client_android.data

import com.devlogs.client_android.PAYMENT_REQUEST_URL
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface PaymentRestful {
    @POST("$PAYMENT_REQUEST_URL")
    suspend fun sendPaymentSms (@Body body: SendPaymentSmsReqBody) : Response<Void>
    data class SendPaymentSmsReqBody (val code: String)
}