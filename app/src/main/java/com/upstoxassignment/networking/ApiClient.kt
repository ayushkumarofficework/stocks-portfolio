package com.upstoxassignment.networking

import com.upstoxassignment.networking.retrofit.HttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    var apiService: ApiService = Retrofit.Builder().baseUrl("https://run.mocky.io/")
        .client(HttpClient.getHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(ApiService::class.java)


}