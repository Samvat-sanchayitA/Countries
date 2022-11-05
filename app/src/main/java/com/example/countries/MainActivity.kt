package com.example.countries

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import android.util.Log
import androidx.compose.ui.tooling.preview.Preview
import com.example.countries.ui.theme.CountriesTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.border
import retrofit2.create
import com.example.countries.network.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CountriesTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    TableScreen()
                    getCountryList()
                }
            }
        }
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
            .border(1.dp, Color.Black)
            .weight(weight)
            .padding(2.dp),
         softWrap = true
    )
}

@Composable
fun TableScreen() {
    // Just a fake data... a Pair of Int and String
    val tableData = (1..100).mapIndexed { index, item ->
        index to "Item $index"
    }
    // Each cell of a column must have the same weight.
    val column1Weight = .3f
    val column2Weight = .2f
    val column3Weight = .2f
    val column4Weight = .3f

    // The LazyColumn will be our table. Notice the use of the weights below
    LazyColumn(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        // Here is the header
        item {
            Row(modifier = Modifier.background(Color.Gray)) {
                TableCell(text = "Name", weight = column1Weight)
                TableCell(text = "Region", weight = column2Weight)
                TableCell(text = "Code", weight = column3Weight)
                TableCell(text = "Capital", weight = column4Weight)

            }
        }
        // Here are all the lines of your table.
        items(tableData) {
            val (id, text) = it
            Row(modifier = Modifier.fillMaxWidth()) {
                TableCell(text = id.toString(), weight = column1Weight)
                TableCell(text = text, weight = column2Weight)
                TableCell(text = id.toString(), weight = column3Weight)
                TableCell(text = text + text, weight = column4Weight)
            }
        }
    }
}

fun getCountryList()  {
    val countriesApi = RetrofitClient.getInstance().create(CountriesApi::class.java)

    GlobalScope.launch {
        try {
            val response = countriesApi.getCountries()
            if (response.isSuccessful()) {
                Log.d("ayush: ", response.body().toString())


            } else {

            }
        }catch (Ex:Exception){
            Log.e("Error",Ex.localizedMessage)
        }
    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CountriesTheme {
        TableScreen()
    }
}