package com.example.barkepedia.view.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.barkepedia.model.DogBreed
import com.example.barkepedia.repository.DogRepository
import kotlinx.coroutines.launch

// MainViewModel.kt
class MainViewModel(private val repository: DogRepository) : ViewModel() {
    private val _dogs = MutableLiveData<List<DogBreed>>()
    val dogs: LiveData<List<DogBreed>> get() = _dogs

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun fetchDogs(forceRefresh: Boolean = false) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val dogList = repository.getDogs(forceRefresh)
                _dogs.value = dogList
            } catch (e: Exception) {
                _error.value = "Failed to load dogs: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}

