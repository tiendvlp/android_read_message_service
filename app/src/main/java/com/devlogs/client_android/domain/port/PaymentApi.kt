package com.devlogs.client_android.domain.port

interface PaymentApi {
    /**
     * @throws ErrorEntity.NetworkErrorEntity when can not send data to server
     * @throws ErrorEntity.InvalidJwtErrorEntity when the server resp failed to decrypt the payload
     * @throws ErrorEntity.BadRequestErrorEntity
     * */
    suspend fun sendPaymentSms (code: String)
}