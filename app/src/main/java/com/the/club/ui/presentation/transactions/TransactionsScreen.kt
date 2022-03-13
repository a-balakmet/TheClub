package com.the.club.ui.presentation.transactions

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.gson.Gson
import kotlinx.coroutines.launch
import com.the.club.R
import com.the.club.common.CommonKeys.noNetwork
import com.the.club.common.ktx.formatWithThousand
import com.the.club.ui.commonComponents.*
import com.the.club.ui.navigation.Screen
import com.the.club.ui.presentation.transactions.components.BurningItem
import com.the.club.ui.presentation.transactions.components.DateButton
import com.the.club.ui.presentation.transactions.components.SwitchButton
import com.the.club.ui.presentation.transactions.components.TransactionItem
import com.the.club.ui.theme.*

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun TransactionsScreen(navController: NavController, bonuses: Long) {
    val viewModel = hiltViewModel<TransactionsViewModel>()
    BackHandler(onBack = navController::popBackStack)
    var isTransactions by remember { mutableStateOf(true) }
    var isStart by remember { mutableStateOf(true) }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )
    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetShape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
        sheetContent = {
            if (isStart) {
                DatePickerBottomDialog(
                    dateValue = viewModel.dateFrom.value,
                    iBirthday = false,
                    onClick = { date ->
                        coroutineScope.launch {
                            bottomSheetScaffoldState.bottomSheetState.collapse()
                        }
                        date?.let { theDate ->
                            if (isStart) viewModel.updateDateFrom(theDate)
                            else viewModel.updateDateTo(theDate)
                        }
                    })
            } else {
                DatePickerBottomDialog(
                    dateValue = viewModel.dateTo.value,
                    iBirthday = false,
                    onClick = { date ->
                        coroutineScope.launch {
                            bottomSheetScaffoldState.bottomSheetState.collapse()
                        }
                        date?.let { theDate ->
                            if (isStart) viewModel.updateDateFrom(theDate)
                            else viewModel.updateDateTo(theDate)
                        }
                    })
            }
        },
        sheetPeekHeight = 0.dp
    ) {
        Surface(color = MaterialTheme.colors.background) {
            Column(modifier = Modifier.fillMaxSize()) {
                // toolbar
                MainToolbar(
                    homeIcon = Icons.Filled.ArrowBackIos,
                    onClickHome = { navController.popBackStack() },
                    title = stringResource(id = R.string.transactions_title),
                    menuIcon = null,
                    onClickIcon = { }
                )
                // switcher between transactions and burning bonuses
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp, top = 30.dp, end = 24.dp)
                        .border(width = 1.dp, color = pink, shape = RoundedCornerShape(10.dp))
                ) {
                    SwitchButton(
                        modifier = Modifier.weight(1F),
                        text = stringResource(id = R.string.transaction),
                        isActive = isTransactions
                    ) {
                        coroutineScope.launch {
                            bottomSheetScaffoldState.bottomSheetState.collapse()
                        }
                        isTransactions = true
                    }
                    SwitchButton(
                        modifier = Modifier.weight(1F),
                        text = stringResource(id = R.string.burning),
                        isActive = !isTransactions
                    ) {
                        coroutineScope.launch {
                            bottomSheetScaffoldState.bottomSheetState.collapse()
                        }
                        isTransactions = false
                        viewModel.loadBurnings()
                    }
                }
                // bonuses row
                Row(
                    modifier = Modifier.padding(all = 24.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.my_bonuses),
                        style = Typography.h2,
                        color = textColor(),
                        modifier = Modifier.weight(1F)
                    )
                    Text(
                        text = bonuses.formatWithThousand(),
                        style = Typography.h2,
                        color = textColor()
                    )
                }
                // container for bonuses
                if (isTransactions) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 24.dp)
                    ) {
                        // row for period settings
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            DateButton(
                                modifier = Modifier.weight(1F),
                                textDate = viewModel.dateFrom.value,
                                onClick = {
                                    isStart = true
                                    coroutineScope.launch {
                                        if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                                            bottomSheetScaffoldState.bottomSheetState.expand()
                                        } else {
                                            bottomSheetScaffoldState.bottomSheetState.collapse()
                                        }
                                    }
                                }
                            )
                            DateButton(
                                modifier = Modifier.weight(1F),
                                textDate = viewModel.dateTo.value,
                                onClick = {
                                    isStart = false
                                    coroutineScope.launch {
                                        if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                                            bottomSheetScaffoldState.bottomSheetState.expand()
                                        } else {
                                            bottomSheetScaffoldState.bottomSheetState.collapse()
                                        }
                                    }
                                }
                            )
                        }
                        // container for list of transactions
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .align(alignment = Alignment.CenterHorizontally)
                        ) {
                            viewModel.transactionsState.value.let {
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
                                if (it.isLoadingMore) {
                                    Column(modifier = Modifier.align(alignment = Alignment.BottomCenter)) {
                                        Logo(
                                            modifier = Modifier.align(Alignment.CenterHorizontally),
                                            colors = LoadingColorShades,
                                            isAnimate = false,
                                            text = stringResource(id = R.string.loading),
                                            textColor = textColor()
                                        )
                                    }
                                }
                                if (it.transactions.isNotEmpty()) {
                                    if (listState.layoutInfo.visibleItemsInfo.lastIndex + listState.firstVisibleItemIndex == it.transactions.size - 1) {
                                        viewModel.loadTransactions(false)
                                    }
                                    LazyColumn(
                                        modifier = Modifier
                                            .padding(vertical = 16.dp)
                                            .fillMaxSize(),
                                        state = listState,
                                        verticalArrangement = Arrangement.spacedBy(8.dp),
                                        //state = listState
                                    ) {
                                        itemsIndexed(it.transactions) { index, aTransaction ->
                                            TransactionItem(transaction = aTransaction, onClick = {
                                                coroutineScope.launch {
                                                    bottomSheetScaffoldState.bottomSheetState.collapse()
                                                }
                                                val theTransaction = Gson().toJson(aTransaction)
                                                val transactionId = aTransaction.id
                                                val shopId = aTransaction.actorId
                                                navController.navigate(Screen.TransactionProductsScreen.route + "/$theTransaction/$transactionId/$shopId")
                                            })
                                            if (index < it.transactions.lastIndex)
                                                Divider(modifier = Modifier.padding(top = 8.dp))
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
                                            text = if (it.error == noNetwork) stringResource(id = R.string.no_internet_connection) else it.error,
                                            textColor = gray
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                // container for schedule of burnings
                else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 24.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            text = stringResource(id = R.string.burning_term),
                            style = Typography.body1,
                            color = gray
                        )
                        Text(
                            text = stringResource(id = R.string.burning_term_second),
                            style = Typography.body1,
                            color = gray
                        )
                        Text(
                            text = stringResource(id = R.string.burning_term_third),
                            style = Typography.body1,
                            color = gray
                        )
                        Text(
                            text = stringResource(id = R.string.burning_term_fourth),
                            style = Typography.body1,
                            color = gray
                        )
                        Text(
                            text = stringResource(id = R.string.burn_schedule),
                            style = Typography.h2,
                            color = textColor(),
                            modifier = Modifier.padding(top = 24.dp)
                        )
                        // container for list of burnings
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .align(alignment = Alignment.CenterHorizontally)
                        ) {
                            viewModel.burningsState.value.let {
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
                                if (it.burnings.isNotEmpty()) {
                                    Column(
                                        modifier = Modifier.padding(vertical = 16.dp)
                                    ) {
                                        for (aBurning in it.burnings) {
                                            BurningItem(
                                                burning = aBurning,
                                                showDivider = it.burnings[it.burnings.lastIndex] != aBurning
                                            )
                                        }
                                    }
                                }
                                if (it.isEmptyList) {
                                    Column(modifier = Modifier.align(alignment = Alignment.Center)) {
                                        NoDataMessage(modifier = Modifier.align(Alignment.CenterHorizontally), icon = R.drawable.ic_burning)
                                    }
                                }
                                if (it.error.isNotBlank()) {
                                    Column(modifier = Modifier.align(alignment = Alignment.Center)) {
                                        Logo(
                                            modifier = Modifier.align(Alignment.CenterHorizontally),
                                            colors = GrayColorShades,
                                            isAnimate = false,
                                            text = if (it.error == noNetwork) stringResource(id = R.string.no_internet_connection) else it.error,
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
    }
}