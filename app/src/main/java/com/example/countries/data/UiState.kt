package com.example.countries.data

sealed class UiState {
    object START : UiState()
    object LOADING : UiState()
    data class SUCCESS(val countries: List<Country>) : UiState()
    data class FAILURE(val message: String) : UiState()
}
