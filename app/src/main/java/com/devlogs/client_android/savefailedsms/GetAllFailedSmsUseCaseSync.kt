package com.devlogs.client_android.savefailedsms

import com.devlogs.client_android.common.coroutine.BackgroundDispatcher
import com.devlogs.client_android.domain.entity.PaymentSmsEntity
import com.devlogs.client_android.domain.port.SmsLocalDbApi
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetAllFailedSmsUseCaseSync @Inject constructor(private val smsLocalDbApi: SmsLocalDbApi) {
    sealed class Result {
        data class Success(val data: List<PaymentSmsEntity>) : Result()
        data class GeneralError(val errorMessage: String) : Result()
    }

    suspend fun execute(sms: PaymentSmsEntity)  : Result = withContext(BackgroundDispatcher) {
        Result.Success(smsLocalDbApi.getAll().map {
            PaymentSmsEntity(it.createdDate!!, it.content!!, it.address!!)
        })
    }
    }
