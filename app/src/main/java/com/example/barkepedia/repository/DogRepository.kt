package com.example.barkepedia.repository

import android.util.Log
import com.example.barkepedia.api.ApiService
import com.example.barkepedia.model.DogBreed

class DogRepository(private val apiService: ApiService) {
    suspend fun getDogs(): List<DogBreed> {
        val response = apiService.getDogBreeds()
        Log.d("API_RESPONSE", "Fetched ${response.size} dogs")
        return response
    }
}