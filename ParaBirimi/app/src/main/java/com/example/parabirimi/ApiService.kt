package com.example.parabirimi

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import com.example.parabirimi.Model.ExchangeResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.Response

interface ApiService {
    @GET("latest")
   suspend fun getExchangeRates(
        @Query("from") base: String,
        @Query("to") symbols: String
    ): Response<ExchangeResponse>
}
