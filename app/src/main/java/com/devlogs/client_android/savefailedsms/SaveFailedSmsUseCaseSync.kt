package com.devlogs.client_android.savefailedsms

import com.devlogs.client_android.common.coroutine.BackgroundDispatcher
import com.devlogs.client_android.data.offline.PaymentSmsRealmObject
import com.devlogs.client_android.domain.entity.PaymentSmsEntity
import com.devlogs.client_android.domain.port.SmsLocalDbApi
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SaveFailedSmsUseCaseSync @Inject constructor(private val smsLocalDbApi: SmsLocalDbApi) {
    sealed class Result {
        class Success() : Result()
        data class GeneralError(val errorMessage: String) : Result()
    }

    suspend fun execute(sms: PaymentSmsEntity) : Result = withContext(BackgroundDispatcher) {
        smsLocalDbApi.saveMessage(PaymentSmsRealmObject(address = sms.address, content = sms.content, createdDate = sms.createdDate))
        Result.Success()
    }

}