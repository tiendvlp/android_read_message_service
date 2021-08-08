package com.devlogs.client_android.data.offline

import com.devlogs.client_android.domain.port.SmsLocalDbApi
import io.realm.Realm
import io.realm.RealmConfiguration
import javax.inject.Inject

class SmsLocalDbApiImp @Inject constructor(realmConfiguration: RealmConfiguration) : SmsLocalDbApi {
    private val realmInstance = Realm.getInstance(realmConfiguration)

    override suspend fun saveMessage(paymentSms: PaymentSmsRealmObject) {
        realmInstance.executeTransaction {
            it.copyToRealmOrUpdate(paymentSms)
        }
    }

    override suspend fun getAll(): List<PaymentSmsRealmObject> {
        val result = realmInstance.where(PaymentSmsRealmObject::class.java)
            .findAll()
        return result.toList()
    }

    override suspend fun clear() {
        realmInstance.delete(PaymentSmsRealmObject::class.java)
    }
}