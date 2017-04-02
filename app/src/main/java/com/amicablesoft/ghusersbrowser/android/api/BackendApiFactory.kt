package com.amicablesoft.ghusersbrowser.android.api

import android.util.Log
import com.google.gson.GsonBuilder
import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class BackendApiFactory : ApiFactory {
    private val baseUrl = "https://api.github.com/"
    private val login = "amicablesoft-test"
    private val token = "46f2d64584bb5c36676ef19ed83f2960d61b00ba"
    private val gson = GsonBuilder().create()!!
    private val retrofit: Retrofit

    init {
        val httpClientBuilder = OkHttpClient.Builder()

        httpClientBuilder.addInterceptor { chain ->
            val original = chain.request()
            val authToken = Credentials.basic(login, token)
            val requestBuilder = original.newBuilder()
                .addHeader("Authorization", authToken)
                .method(original.method(), original.body())
            val request = requestBuilder.build()
            chain.proceed(request)
        }

        val loggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message ->
            if (message != null) {
                Log.i("Retrofit", message)
            }
        })
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        httpClientBuilder.addInterceptor(loggingInterceptor)

        retrofit = Retrofit.Builder()
            .client(httpClientBuilder.build())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(baseUrl).build()
    }

    override fun <T> create(serviceClass:Class<T>) : T {
        return retrofit.create(serviceClass)
    }
}