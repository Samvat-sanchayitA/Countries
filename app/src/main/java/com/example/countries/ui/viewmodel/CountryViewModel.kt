package com.example.countries.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.countries.R
import com.example.countries.data.UiState
import com.example.countries.network.CountryApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CountryViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiUiState = MutableStateFlow<UiState>(UiState.START)
    val uiState: StateFlow<UiState> = _uiUiState

    private val errorMsg = application.resources.getString(R.string.something_went_wrong)
    private val noDataMsg = application.resources.getString(R.string.no_data_available)

    init {
        loadCountries()
    }

    private fun loadCountries() = viewModelScope.launch {

        _uiUiState.emit(UiState.LOADING)
        try {
            val countryList =
                withContext(Dispatchers.IO) { CountryApi.retrofitService.getCountries() }
            if (countryList.isNotEmpty()) {
                _uiUiState.emit(UiState.SUCCESS(countryList))
            } else {
                _uiUiState.emit(UiState.FAILURE(noDataMsg))
            }
        } catch (e: Exception) {
            _uiUiState.emit(UiState.FAILURE(e.localizedMessage ?: errorMsg))
        }
    }

}

