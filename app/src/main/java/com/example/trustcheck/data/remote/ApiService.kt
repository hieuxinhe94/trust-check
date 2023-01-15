package com.example.trustcheck.data.remote

import com.example.trustcheck.data.models.PhoneData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    companion object {
        const val BASE_API_URL = "https://trustphone-api.herokuapp.com/"
    }

    @GET("phones/search/{number}")
    fun searchPhone(@Path("number") number: String?): Call<List<PhoneData?>?>?

    @GET("phones/search/{number}")
    fun findPhone(@Path("number") number: String?): Call<PhoneData?>?
}