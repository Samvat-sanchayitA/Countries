package com.example.countries.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import com.example.countries.data.Country

interface CountriesApi {
    @GET("countries.json")
    suspend fun getCountries() : Response<List<Country>>
}