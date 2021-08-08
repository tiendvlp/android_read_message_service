package com.devlogs.client_android.common.di

import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.devlogs.client_android.HOST
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.realm.RealmConfiguration
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ApplicationModule {
    @Provides
    @Singleton
    fun provideSharedPreference(appContext: Application): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(appContext)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient() =
        OkHttpClient.Builder()
            .callTimeout(21, TimeUnit.SECONDS)
            .connectTimeout(21, TimeUnit.SECONDS).build()

    @Provides
    @Singleton
    fun providePaymentServerRetrofit (client: OkHttpClient) : Retrofit {
        return Retrofit.Builder()
            .baseUrl(HOST)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideRealmInstance () : RealmConfiguration {
        return RealmConfiguration.Builder()
            .name("CHATTY_LOCAL_DB")
            .schemaVersion(1)
            .allowQueriesOnUiThread(false)
            .allowWritesOnUiThread(false)
            .build()
    }
}