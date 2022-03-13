package com.the.club.ui.presentation.transactions

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.the.club.R
import com.the.club.common.CommonKeys
import com.the.club.common.ktx.formatWithThousand
import com.the.club.domain.model.transactions.Transaction
import com.the.club.ui.commonComponents.BackHandler
import com.the.club.ui.commonComponents.Logo
import com.the.club.ui.commonComponents.MainToolbar
import com.the.club.ui.commonComponents.NoDataMessage
import com.the.club.ui.presentation.transactions.components.TransactionData
import com.the.club.ui.presentation.transactions.components.TransactionProductsItem
import com.the.club.ui.presentation.transactions.components.WinChanceItem
import com.the.club.ui.theme.*

@Composable
fun TransactionProductsScreen(navController: NavController, transaction: Transaction) {
    val viewModel = hiltViewModel<TransactionProductsViewModel>()
    BackHandler(onBack = navController::popBackStack)
    Surface(color = MaterialTheme.colors.background) {
        Column(modifier = Modifier.fillMaxSize()) {
            // toolbar
            MainToolbar(
                homeIcon = Icons.Filled.ArrowBackIos,
                onClickHome = { navController.popBackStack() },
                title = stringResource(id = R.string.receipt_details),
                menuIcon = null,
                onClickIcon = { }
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, top = 30.dp, end = 24.dp, bottom = 16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "${
                        stringResource(id = R.string.purchase_sum)
                    } ${
                        transaction.checkAmount.formatWithThousand()
                    } ${
                        stringResource(id = R.string.tenge_symbol)
                    }",
                    style = Typography.h2,
                    color = textColor()

                )
                TransactionData(transaction = transaction, shopAddress = viewModel.shopAddress)
                // chances to win something
                viewModel.winningState.value.let {
                    if(it.chances.isNotEmpty()) {
                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)) {
                            Text(
                                text = stringResource(id = R.string.win_chance),
                                style = Typography.h4,
                                color = textColor()
                            )
                            for (chance in it.chances) {
                                WinChanceItem(chance = chance)
                            }
                        }
                    }
                }
                // products in transaction
                Box(modifier = Modifier
                    .fillMaxSize()
                    .align(alignment = Alignment.CenterHorizontally)
                ) {
                    viewModel.productsState.value.let {
                        if (it.isLoading) {
                            Column(modifier = Modifier.align(alignment = Alignment.Center)) {
                                Logo(
                                    modifier = Modifier.align(Alignment.CenterHorizontally),
                                    colors = MainCardColors,
                                    isAnimate = true,
                                    text = stringResource(id = R.string.loading),
                                    textColor = textColor()
                                )
                            }
                        }
                        if (it.products.isNotEmpty()) {
                            Column(modifier = Modifier.fillMaxSize()) {
                                Divider(color = lightGray, thickness = 1.dp, modifier = Modifier.padding(vertical = 16.dp))
                                Text(
                                    text = stringResource(id = R.string.receipt_content),
                                    style = Typography.h5,
                                    color = textColor()
                                )
                                for (product in it.products) {
                                    TransactionProductsItem(product = product)
                                }
                            }
                        }
                        if (it.isEmptyList) {
                            Column(modifier = Modifier.align(alignment = Alignment.Center)) {
                                NoDataMessage(modifier = Modifier.align(Alignment.CenterHorizontally), icon = R.drawable.ic_transactions)
                            }
                        }
                        if (it.error.isNotBlank()) {
                            Column(modifier = Modifier.align(alignment = Alignment.Center)) {
                                Logo(
                                    modifier = Modifier.align(Alignment.CenterHorizontally),
                                    colors = GrayColorShades,
                                    isAnimate = false,
                                    text = if (it.error == CommonKeys.noNetwork) stringResource(id = R.string.no_internet_connection) else it.error,
                                    textColor = gray
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}