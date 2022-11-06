package com.example.countries.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL =
    "https://gist.githubusercontent.com/peymano-wmt/32dcb892b06648910ddd40406e37fdab/raw/db25946fd77c5873b0303b858e861ce724e0dcd0/"

private val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

object CountryApi {
    val retrofitService: CountriesApiService by lazy {
        retrofit.create(CountriesApiService::class.java)
    }
}