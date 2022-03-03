package com.upstoxassignment.networking

import com.upstoxassignment.model.PortfolioModel
import retrofit2.http.GET
import retrofit2.http.Headers


interface ApiService {
    @Headers("Accept: application/json")
    @GET("/v3/6d0ad460-f600-47a7-b973-4a779ebbaeaf")
    suspend fun getPortfolioData(): PortfolioModel
}