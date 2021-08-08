package com.devlogs.client_android.data.offline

import io.realm.RealmObject

open class PaymentSmsRealmObject : RealmObject {
    var createdDate : Long? = null
    var content: String? = null
    var address: String? = null

    constructor(address: String, content: String, createdDate: Long) {
        this.address = address
        this.content = content
        this.createdDate = createdDate
    }

    constructor()

}