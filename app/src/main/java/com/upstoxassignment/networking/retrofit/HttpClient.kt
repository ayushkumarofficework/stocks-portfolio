package com.upstoxassignment.networking.retrofit

import okhttp3.OkHttpClient


object HttpClient {
    var okClient: OkHttpClient = OkHttpClient.Builder().build()

    fun getHttpClient(): OkHttpClient {
        return okClient
    }
}