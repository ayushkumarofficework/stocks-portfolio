package com.upstoxassignment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.Indication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

import androidx.lifecycle.ViewModelProvider
import com.upstoxassignment.model.PortfolioItem
import com.upstoxassignment.state.PortfolioState
import com.upstoxassignment.viewmodel.PortfolioActivityViewModel

class PortfolioActivity : AppCompatActivity() {

    lateinit var portfolioActivityViewModel: PortfolioActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        portfolioActivityViewModel =
            ViewModelProvider(this).get(PortfolioActivityViewModel::class.java)
        setContent {
            PortfolioView({ portfolioActivityViewModel.onPortfolioFooterClicked() })
        }
        supportActionBar?.title = "Portfolio"
    }

    @Composable
    fun PortfolioView(onFooterClick: () -> Unit) {
        val portfolioState = portfolioActivityViewModel._portfolioState.observeAsState()
        val portfolioItems = portfolioState.value?.data?.data ?: emptyList()
        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(items = portfolioItems) { item: PortfolioItem ->
                    PortfolioItemComposable(item)
                }
            }
            PortfolioFooter(portfolioState.value, onFooterClick)
        }
    }

    @Composable
    fun PortfolioFooter(value: PortfolioState?, onFooterClick: () -> Unit) {
        var footerState = portfolioActivityViewModel._isFooterExpanded.observeAsState()
        if (footerState.value == true) PortfolioFooterExpanded(
            value,
            onFooterClick = onFooterClick
        ) else PortfolioFooterCollapsed(value, onFooterClick = onFooterClick)
    }

    @Composable
    private fun PortfolioFooterExpanded(
        portfolioState: PortfolioState?,
        onFooterClick: () -> Unit
    ) {
        Column(modifier = Modifier
            .background(color = Color(0xfff7faf3))
            .clickable { onFooterClick() }
            .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 10.dp)
        ) {
            Row(modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)) {
                Text(
                    text = "Current Value",
                    modifier = Modifier.weight(1f),
                    color = Color(0xff9aa09a)
                )
                Text(text = stringResource(id = R.string.currency_rupee, formatArgs = arrayOf(portfolioState?.currentValue.toString())))
            }

            Row(modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)) {
                Text(
                    text = "Total Investment",
                    modifier = Modifier.weight(1f),
                    color = Color(0xff9aa09a)
                )
                Text(text = stringResource(id = R.string.currency_rupee, formatArgs = arrayOf(portfolioState?.investedValue.toString())))
            }

            Row(modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)) {
                Text(
                    text = "Today's Profit & Loss",
                    modifier = Modifier.weight(1f),
                    color = Color(0xff9aa09a)
                )
                Text(
                    text = stringResource(id = R.string.currency_rupee, formatArgs = arrayOf(String.format("%.2f", portfolioState?.todayProfit))),
                    color = if (portfolioState?.todayProfit?.compareTo(0) ?: 0 >= 0) Color(
                        0xff078610
                    ) else Color.Red
                )
            }

            Divider(modifier = Modifier.height(1.dp), color = Color.Gray)

            StickyFooter(portfolioState = portfolioState, true)

        }
    }

    @Composable
    private fun StickyFooter(portfolioState: PortfolioState?, isExpanded: Boolean) {
        Row(modifier = Modifier.padding(top = 10.dp)) {
            Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Profit & Loss", color = Color(0xff9aa09a))
                Spacer(modifier = Modifier.width(10.dp))
                Image(
                    painter = painterResource(id = if (isExpanded) R.drawable.ic_chevron_down else R.drawable.ic_chevron_up_grey),
                    contentDescription = ""
                )
            }

            Text(
                text = stringResource(id = R.string.currency_rupee, formatArgs = arrayOf(String.format("%.2f", portfolioState?.totalProfit))),
                color = if (portfolioState?.totalProfit?.compareTo(0) ?: 0 >= 0) Color(0xff078610) else Color.Red
            )
        }
    }

    @Composable
    private fun PortfolioFooterCollapsed(
        portfolioState: PortfolioState?,
        onFooterClick: () -> Unit
    ) {

        Column(modifier = Modifier
            .background(color = Color(0xfff7faf3))
            .clickable(onClick = { onFooterClick() })
            .padding(start = 20.dp, end = 20.dp, bottom = 10.dp)
        ) {

            Divider(modifier = Modifier.height(1.dp), color = Color.Gray)

            StickyFooter(portfolioState = portfolioState, false)

        }
    }

    @Composable
    fun PortfolioItemComposable(portfolioItem: PortfolioItem) {
        Column(
            modifier = Modifier.padding(
                start = 20.dp,
                end = 20.dp,
                top = 10.dp,
                bottom = 4.dp
            )
        ) {
            Row() {
                Text(
                    text = portfolioItem.symbol,
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.Bold
                )
                Row() {
                    Text(text = "LTP: ", color = Color(0xff9aa09a))
                    Text(text = stringResource(id = R.string.currency_rupee, formatArgs = arrayOf(portfolioItem.ltp.toString())))
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Row() {
                Row(modifier = Modifier.weight(1f)) {
                    Text(text = "NET QTY: ", color = Color(0xff9aa09a))
                    Text(text = portfolioItem.quantity.toString())
                }
                Row() {
                    Text(text = "P&L: ", color = Color(0xff9aa09a))
                    Text(
                        text = stringResource(id = R.string.currency_rupee, formatArgs = arrayOf(String.format("%.2f", portfolioItem.profitLoss))),
                        color = if (portfolioItem.profitLoss >= 0) Color(0xff078610) else Color.Red
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = Color.LightGray)
        }
    }
}

