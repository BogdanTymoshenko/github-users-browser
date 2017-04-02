package com.amicablesoft.ghusersbrowser.android.api

import android.util.Log
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by bogdan on 4/1/17.
 */
interface ApiFactory {
    fun <T> create(serviceClass: Class<T>) : T
}
