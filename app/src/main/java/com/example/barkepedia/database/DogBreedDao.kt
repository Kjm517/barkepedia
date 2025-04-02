package com.example.barkepedia.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DogBreedDao {
    @Query("SELECT * FROM dog_breeds ORDER BY name ASC")
    suspend fun getAllDogBreeds(): List<DogBreedEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(dogs: List<DogBreedEntity>)

    @Query("DELETE FROM dog_breeds")
    suspend fun deleteAll()
}