package com.example.parabirimi

import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.frankfurter.app/") // API'nin base URL'si
            .addConverterFactory(GsonConverterFactory.create()) // JSON'ı parse etmek için
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create()) // RxJava3 Call Adapter
            .build()
            .create(ApiService::class.java)
    }
}
