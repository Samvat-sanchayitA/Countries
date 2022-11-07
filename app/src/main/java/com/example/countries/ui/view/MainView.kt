package com.example.countries.ui.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.AlertDialog
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import com.example.countries.R
import com.example.countries.data.Country
import com.example.countries.data.UiState
import com.example.countries.ui.viewmodel.CountryViewModel
import com.example.countries.util.ConnectivityObserver
import com.example.countries.util.NetworkConnectivityObserver

@Composable
fun MainView(viewModel: CountryViewModel) {

    // val uiState by viewModel.uiState.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val uiStateLifecycleAware = remember(viewModel.uiState, lifecycleOwner) {
        viewModel.uiState.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
    }
    val uiState by uiStateLifecycleAware.collectAsState(initial = UiState.START)
    val context = LocalContext.current
    val connectivityObserver = NetworkConnectivityObserver(context)
    val connectionStatus by connectivityObserver.observe().collectAsState(
        initial = ConnectivityObserver.Status.Available
    )

    if (connectionStatus == ConnectivityObserver.Status.Available) {
        when (uiState) {
            is UiState.START -> {
            }
            is UiState.LOADING -> {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator()
                }
            }
            is UiState.FAILURE -> {
                val error = (uiState as UiState.FAILURE).message
                ErrorDialog(error)
            }
            is UiState.SUCCESS -> {
                val countryList = (uiState as UiState.SUCCESS).countries
                TableScreen(countryList, viewModel)
            }
        }
    } else {
        ErrorDialog(message = stringResource(id = R.string.no_internet_available))
    }
}

@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float
) {
    Text(
        text = text,
        modifier = Modifier
            .weight(weight)
            .padding(3.dp),
        softWrap = true
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TableScreen(countryList: List<Country>, viewModel: CountryViewModel) {
    val column1Weight = .3f
    val column2Weight = .2f
    val column3Weight = .2f
    val column4Weight = .3f


    val savedIndex by viewModel.savedIndex.collectAsState()
    val savedOffset by viewModel.savedOffset.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current

    val listState = rememberLazyListState(savedIndex, savedOffset)

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .padding(6.dp)
    ) {

        stickyHeader {
            Row(modifier = Modifier.background(Color.Gray)) {
                TableCell(text = "Name", weight = column1Weight)
                TableCell(text = "Region", weight = column2Weight)
                TableCell(text = "Code", weight = column3Weight)
                TableCell(text = "Capital", weight = column4Weight)

            }
        }

        items(countryList) {
            Row(modifier = Modifier.fillMaxWidth()) {
                TableCell(text = it.name, weight = column1Weight)
                TableCell(text = it.region, weight = column2Weight)
                TableCell(text = it.code, weight = column3Weight)
                TableCell(text = it.capital, weight = column4Weight)
            }
        }
    }
    DisposableEffect(lifecycleOwner) {
        onDispose {
            val lastIndex = listState.firstVisibleItemIndex
            val lastOffset = listState.firstVisibleItemScrollOffset
            viewModel.saveScrollPosition(lastIndex, lastOffset)
        }

    }
}

@Composable
fun ErrorDialog(message: String) {
    Row(modifier = Modifier.padding(8.dp)) {
        Image(
            painterResource(id = R.drawable.ic_fragile_broken),
            contentDescription = "",
            contentScale = ContentScale.Crop
        )
        Text(text = message, modifier = Modifier.padding(16.dp))
    }
    var openDialog by remember { mutableStateOf(true) }
    if (openDialog) {
        AlertDialog(
            onDismissRequest = {
                openDialog = false
            },
            title = {
                Text(text = stringResource(R.string.problem_occurred))
            },
            text = {
                Text(message, fontSize = 16.sp)
            },

            confirmButton = {},
            dismissButton = {
                TextButton(
                    onClick = {
                        openDialog = false
                    }
                ) {
                    Text("Dismiss")
                }
            },
        )
    }
}

