package com.example.countries.network

import com.example.countries.data.Country
import retrofit2.http.GET

interface CountriesApiService {
    @GET("countries.json")
    suspend fun getCountries() : List<Country>
}