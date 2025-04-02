package com.example.barkepedia.api

import com.example.barkepedia.model.DogBreed
import retrofit2.http.GET

interface ApiService {
    @GET("breeds")
    suspend fun getDogBreeds(): List<DogBreed>
}
