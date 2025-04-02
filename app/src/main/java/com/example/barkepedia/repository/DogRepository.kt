package com.example.barkepedia.repository

import android.util.Log
import com.example.barkepedia.Api.ApiService
import com.example.barkepedia.model.DogBreed
import com.example.dogapp.data.api.RetrofitInstance

class DogRepository(private val apiService: ApiService) {
    suspend fun getDogs(): List<DogBreed> {
        val response = apiService.getDogBreeds()
        Log.d("API_RESPONSE", "Fetched ${response.size} dogs")
        return response
    }
}