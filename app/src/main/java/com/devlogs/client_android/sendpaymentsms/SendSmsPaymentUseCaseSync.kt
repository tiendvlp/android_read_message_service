package com.devlogs.client_android.sendpaymentsms

import com.devlogs.client_android.common.coroutine.BackgroundDispatcher
import com.devlogs.client_android.domain.entity.PaymentSmsEntity
import com.devlogs.client_android.domain.errorentity.ErrorEntity
import com.devlogs.client_android.domain.port.PaymentApi
import com.devlogs.client_android.encrypt.Encrypter
import com.devlogs.client_android.sendpaymentsms.SendSmsPaymentUseCaseSync.Result.*
import com.google.gson.Gson
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SendSmsPaymentUseCaseSync @Inject constructor(
    private val encrypter: Encrypter,
    private val paymentApi: PaymentApi
) {
    sealed class Result {
        class Success : Result()
        class NetworkError : Result()
        data class GeneralError(val errorMessage: String) : Result()
    }

    suspend fun execute(paymentSms: PaymentSmsEntity): Result = withContext(BackgroundDispatcher) {
        val payload = Gson().toJson(paymentSms)
        val code = encrypter.createJWT(payload)
        if (code == null) {
            GeneralError("Bad Jwt")
        }
        try {
            paymentApi.sendPaymentSms(code!!)
            Success()
        } catch (e: ErrorEntity.NetworkErrorEntity) {
            NetworkError()
        } catch (e: ErrorEntity.InvalidJwtErrorEntity) {
            GeneralError("Invalid Jwt code")
        } catch (e: ErrorEntity.BadRequestErrorEntity) {
            GeneralError("Bad request")
        }
    }


}