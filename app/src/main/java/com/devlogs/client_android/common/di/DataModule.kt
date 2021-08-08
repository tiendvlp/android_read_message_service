package com.devlogs.client_android.common.di

import com.devlogs.client_android.data.PaymentApiImp
import com.devlogs.client_android.domain.port.SmsLocalDbApi
import com.devlogs.client_android.data.offline.SmsLocalDbApiImp
import com.devlogs.client_android.domain.port.PaymentApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ServiceComponent

@InstallIn(ActivityComponent::class, ServiceComponent::class)
@Module
class DataModule {
    @Provides
    fun providePaymentApi (paymentApiImp: PaymentApiImp) : PaymentApi = paymentApiImp
    @Provides
    fun provideSmsLocalDbApi (smsLocalDbApiImp: SmsLocalDbApiImp) : SmsLocalDbApi = smsLocalDbApiImp
}