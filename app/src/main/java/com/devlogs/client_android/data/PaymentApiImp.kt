package com.devlogs.client_android.data

import com.devlogs.client_android.domain.errorentity.ErrorEntity.*
import com.devlogs.client_android.domain.port.PaymentApi
import retrofit2.HttpException
import retrofit2.Retrofit
import javax.inject.Inject

class PaymentApiImp @Inject constructor(private val retrofit : Retrofit) : PaymentApi {
    override suspend fun sendPaymentSms(code: String) {
        try {
            val reqBody = PaymentRestful.SendPaymentSmsReqBody(code)
            val client = retrofit.create(PaymentRestful::class.java)
            val response = client.sendPaymentSms(reqBody)

            if (!response.isSuccessful) {
                throw BadRequestErrorEntity()
            }

            if (response.code() == 400) {
                throw InvalidJwtErrorEntity()
            }

        } catch (e: HttpException) {
            throw NetworkErrorEntity()
        }
    }
}