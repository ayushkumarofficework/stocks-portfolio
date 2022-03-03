package com.upstoxassignment.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upstoxassignment.model.PortfolioModel


import com.upstoxassignment.networking.ApiClient
import com.upstoxassignment.state.PortfolioState
import kotlinx.coroutines.launch


class PortfolioActivityViewModel : ViewModel() {

    private var portfolioState: MutableLiveData<PortfolioState> = MutableLiveData()
    val _portfolioState: LiveData<PortfolioState> = portfolioState

    private var isFooterExpanded: MutableLiveData<Boolean> = MutableLiveData()
    val _isFooterExpanded: LiveData<Boolean> = isFooterExpanded

    init {
        fetchPortfolio()
    }

    fun fetchPortfolio() {
        viewModelScope.launch {
            val portfolioData = ApiClient.apiService.getPortfolioData()
            val portfolio = getPortfolioState(portfolioData)
            isFooterExpanded.value = false
            portfolioState.value = portfolio
        }
    }

    private fun getPortfolioState(portfolioData: PortfolioModel): PortfolioState {
        var todayProfit = 0.0
        var totalProfit = 0.0
        var currentInvestment = 0.0
        var currentValue = 0.0
        for (portfolioItem in portfolioData.data) {
            currentValue += portfolioItem.ltp * portfolioItem.quantity
            currentInvestment += portfolioItem.avg_price.toDouble() * portfolioItem.quantity
            todayProfit += ((portfolioItem.close - portfolioItem.ltp) * portfolioItem.quantity)
            portfolioItem.profitLoss =
                (portfolioItem.ltp - portfolioItem.avg_price.toDouble()) * portfolioItem.quantity
        }
        totalProfit = currentValue - currentInvestment
        return PortfolioState(
            data = portfolioData,
            todayProfit = todayProfit,
            totalProfit = totalProfit,
            currentValue = currentValue,
            investedValue = currentInvestment
        )
    }

    fun onPortfolioFooterClicked() {
        val isFooterExpandedState = isFooterExpanded.value
        isFooterExpanded.value = !(isFooterExpandedState ?: false)
    }

}