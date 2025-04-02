package com.example.barkepedia.view.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.barkepedia.model.DogBreed
import com.example.barkepedia.repository.DogRepository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: DogRepository) : ViewModel() {

    private val _dogs = MutableLiveData<List<DogBreed>>()
    val dogs: LiveData<List<DogBreed>> get() = _dogs

    fun fetchDogs() {
        viewModelScope.launch {
            try {
                val response = repository.getDogs()
                _dogs.value = response
                Log.d("FETCH_DOGS", "Success: ${response.size} dogs fetched")
            } catch (e: Exception) {
                Log.e("FETCH_DOGS", "Error fetching dogs", e)
            }
        }
    }

}

