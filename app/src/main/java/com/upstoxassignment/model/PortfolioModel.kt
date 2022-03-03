package com.upstoxassignment.model

data class PortfolioModel(val data: List<PortfolioItem>)

data class PortfolioItem(
    var symbol: String,
    var quantity: Int,
    var ltp: Double,
    var close: Double,
    var avg_price: String,
    var profitLoss: Double = 0.0
)