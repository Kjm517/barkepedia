package com.example.barkepedia.view.main

import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.barkepedia.R
import com.example.barkepedia.model.DogBreed
import com.example.barkepedia.network.NetworkHelper
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class DogBreedDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dog_breed_detail)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // Get UI elements using findViewById
        val imageViewDog = findViewById<ImageView>(R.id.imageViewDog)
        val textViewDogName = findViewById<TextView>(R.id.textViewDogName)
        val textViewBreedGroup = findViewById<TextView>(R.id.textViewBreedGroup)
        val textViewOrigin = findViewById<TextView>(R.id.textViewOrigin)
        val textViewBredFor = findViewById<TextView>(R.id.textViewBredFor)
        val chipGroupTemperament = findViewById<ChipGroup>(R.id.chipGroupTemperament)
        val textViewWeightImperial = findViewById<TextView>(R.id.textViewWeightImperial)
        val textViewWeightMetric = findViewById<TextView>(R.id.textViewWeightMetric)
        val textViewHeightImperial = findViewById<TextView>(R.id.textViewHeightImperial)
        val textViewHeightMetric = findViewById<TextView>(R.id.textViewHeightMetric)
        val textViewLifeSpan = findViewById<TextView>(R.id.textViewLifeSpan)

        val dog = intent.getSerializableExtra("dogBreed") as? DogBreed

        dog?.let {
            val toolbarTitleTextView = toolbar.findViewById<TextView>(R.id.toolbarTitle)
            if (toolbarTitleTextView != null) {
                toolbarTitleTextView.text = it.name
            }

            textViewDogName.text = it.name
            textViewBreedGroup.text = if (it.breedGroup.isNullOrEmpty()) "Unknown" else it.breedGroup
            textViewOrigin.text = if (it.origin.isNullOrEmpty()) "Unknown origin" else it.origin
            textViewBredFor.text = if (it.bredFor.isNullOrEmpty()) "Unknown purpose" else it.bredFor

            textViewWeightImperial.text = if (it.weight.imperial.isEmpty()) "Unknown" else "${it.weight.imperial} lb"
            textViewWeightMetric.text = if (it.weight.metric.isEmpty()) "Unknown" else "${it.weight.metric} kg"
            textViewHeightImperial.text = if (it.height.imperial.isEmpty()) "Unknown" else "${it.height.imperial} in"
            textViewHeightMetric.text = if (it.height.metric.isEmpty()) "Unknown" else "${it.height.metric} cm"
            textViewLifeSpan.text = it.lifeSpan

            chipGroupTemperament.removeAllViews() // Clear any existing chips
            it.temperament?.split(", ")?.forEach { trait ->
                val chip = Chip(this)
                chip.text = trait
                chip.isCheckable = false
                chipGroupTemperament.addView(chip)
            }

            NetworkHelper.loadDogImage(this, dog.referenceImageId, imageViewDog)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
