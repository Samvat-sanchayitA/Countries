package com.example.countries

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.*
import androidx.compose.ui.graphics.Color
import com.example.countries.ui.viewmodel.CountryViewModel
import com.example.countries.ui.theme.CountriesTheme
import com.example.countries.ui.view.MainView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CountriesTheme {
                // A surface container using the 'background' color from the theme
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text(getString(R.string.list_countries)) },
                            backgroundColor = Color.Black,
                            contentColor = Color.White
                        )
                    },
                ) {
                    val viewModel: CountryViewModel by viewModels()
                    MainView(viewModel)
                }
            }
        }
    }
}