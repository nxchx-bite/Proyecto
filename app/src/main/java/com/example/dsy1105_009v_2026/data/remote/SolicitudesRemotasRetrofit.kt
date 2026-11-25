package com.example.dsy1105_009v_2026.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SolicitudesRemotasRetrofit {

    private const val BASE_URL =
        "http://10.0.2.2:8080/"

    val api: SolicitudRemotaApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
            .create(SolicitudRemotaApiService::class.java)
    }
}

