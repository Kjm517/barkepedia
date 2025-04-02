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
import android.widget.TextView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.barkepedia.database.AppDatabase
import com.example.barkepedia.model.DogBreed
import com.example.barkepedia.network.NetworkHelper

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
        val errorTextView = findViewById<TextView>(R.id.textViewError)
        val swipeRefreshLayout = findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)

        recyclerViewDogs.layoutManager = LinearLayoutManager(this)

        adapter = DogAdapter(emptyList()) { selectedDog ->
            NetworkHelper.preloadDogImage(this, selectedDog.referenceImageId)
            openDogDetail(selectedDog)
        }
        recyclerViewDogs.adapter = adapter

        // Initialize Room Database and Repository
        val database = AppDatabase.getDatabase(this)
        val repository = DogRepository(
            RetrofitInstance.apiService,
            database.dogBreedDao()
        )

        val factory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        viewModel.dogs.observe(this) { dogs ->
            if (dogs.isNotEmpty()) {
                allDogs = dogs
                adapter.updateData(dogs)
                recyclerViewDogs.visibility = View.VISIBLE
                errorTextView.visibility = View.GONE
                preloadInitialImages(dogs)
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            swipeRefreshLayout.isRefreshing = isLoading
        }

        viewModel.error.observe(this) { errorMessage ->
            if (errorMessage != null && allDogs.isEmpty()) {
                errorTextView.text = errorMessage
                errorTextView.visibility = View.VISIBLE
                recyclerViewDogs.visibility = View.GONE
            } else {
                errorTextView.visibility = View.GONE
            }
        }

        swipeRefreshLayout.setOnRefreshListener {
            viewModel.fetchDogs(forceRefresh = true)
        }

        // Add scroll listener for image preloading
        recyclerViewDogs.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy > 0) {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastVisiblePosition = layoutManager.findLastVisibleItemPosition()

                    val preloadCount = 5
                    val itemCount = adapter.itemCount

                    for (i in lastVisiblePosition + 1 until minOf(lastVisiblePosition + preloadCount, itemCount)) {
                        val dog = allDogs.getOrNull(i) ?: continue
                        NetworkHelper.preloadDogImage(this@MainActivity, dog.referenceImageId)
                    }
                }
            }
        })

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterDogs(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        viewModel.fetchDogs()
    }

    private fun preloadInitialImages(dogs: List<DogBreed>) {
        dogs.take(10).forEach { dog ->
            NetworkHelper.preloadDogImage(this, dog.referenceImageId)
        }
    }

    private fun filterDogs(query: String) {
        if (query.isEmpty()) {
            adapter.updateData(allDogs)
            return
        }

        val lowercaseQuery = query.lowercase()
        val filteredDogs = allDogs.filter { dog ->
            dog.name.contains(lowercaseQuery, ignoreCase = true) ||
                    dog.breedGroup?.contains(lowercaseQuery, ignoreCase = true) == true
        }
        adapter.updateData(filteredDogs)
    }

    private fun openDogDetail(dog: DogBreed) {
        val intent = Intent(this, DogBreedDetailActivity::class.java)
        intent.putExtra("dogBreed", dog)
        startActivity(intent)
    }
}

