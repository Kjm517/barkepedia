package com.example.barkepedia.view.main

import DogAdapter
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.barkepedia.R
import com.example.barkepedia.ViewModel.MainViewModelFactory
import com.example.barkepedia.repository.DogRepository
import com.example.dogapp.data.api.RetrofitInstance
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import com.example.barkepedia.model.DogBreed

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: DogAdapter
    private lateinit var viewModel: MainViewModel
    private var allDogs = listOf<DogBreed>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerViewDogs = findViewById<RecyclerView>(R.id.recyclerViewDogs)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val searchEditText = findViewById<EditText>(R.id.editTextSearch)

        recyclerViewDogs.layoutManager = LinearLayoutManager(this)

        adapter = DogAdapter(emptyList()) { selectedDog ->
            openDogDetail(selectedDog)
        }
        recyclerViewDogs.adapter = adapter

        val repository = DogRepository(RetrofitInstance.apiService)
        val factory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        viewModel.dogs.observe(this) { dogs ->
            if (dogs.isNotEmpty()) {
                allDogs = dogs // Store all dogs for filtering
                adapter.updateData(dogs)
                progressBar.visibility = View.GONE
                Log.d("UI_UPDATE", "RecyclerView updated with ${dogs.size} items")
            } else {
                Log.w("UI_UPDATE", "No data received, check API response")
            }
        }

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterDogs(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        progressBar.visibility = View.VISIBLE
        viewModel.fetchDogs()
    }

    private fun filterDogs(query: String) {
        if (query.isEmpty()) {
            adapter.updateData(allDogs)
            return
        }

        val lowercaseQuery = query.lowercase()
        val filteredDogs = allDogs.filter { dog ->
            dog.name.lowercase().contains(lowercaseQuery) ||
                    (dog.breedGroup?.lowercase()?.contains(lowercaseQuery) ?: false)
        }
        adapter.updateData(filteredDogs)
    }

    private fun openDogDetail(dog: DogBreed) {
        val intent = Intent(this, DogBreedDetailActivity::class.java)
        intent.putExtra("dogBreed", dog)
        startActivity(intent)
    }
}

