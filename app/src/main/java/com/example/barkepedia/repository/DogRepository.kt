package com.example.barkepedia.repository

import android.util.Log
import com.example.barkepedia.api.ApiService
import com.example.barkepedia.database.DogBreedDao
import com.example.barkepedia.database.toEntity
import com.example.barkepedia.model.DogBreed

class DogRepository(
    private val apiService: ApiService,
    private val dogBreedDao: DogBreedDao
) {
    suspend fun getDogs(forceRefresh: Boolean = false): List<DogBreed> {
        val localDogs = dogBreedDao.getAllDogBreeds().map { it.toDogBreed() }

        if (localDogs.isNotEmpty() && !forceRefresh) {
            return localDogs
        }

        return try {
            val remoteDogs = apiService.getDogBreeds()

            // Save to database
            dogBreedDao.deleteAll()
            dogBreedDao.insertAll(remoteDogs.map { it.toEntity() })

            remoteDogs
        } catch (e: Exception) {
            if (localDogs.isNotEmpty()) {
                return localDogs
            }
            throw e
        }
    }
}