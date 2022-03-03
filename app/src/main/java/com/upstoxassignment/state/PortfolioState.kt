package com.upstoxassignment.state

import com.upstoxassignment.model.PortfolioModel

data class PortfolioState(
    var isFooterExpanded: Boolean = false,
    var currentValue: Double = 0.0,
    var investedValue: Double = 0.0,
    var todayProfit: Double = 0.0,
    var totalProfit: Double = 0.0,
    var data: PortfolioModel
)


