package com.example.barkepedia.model

import com.google.gson.annotations.SerializedName

import java.io.Serializable

data class DogBreed(
    val id: Int,
    val name: String,
    @SerializedName("bred_for") val bredFor: String?,
    @SerializedName("breed_group") val breedGroup: String?,
    @SerializedName("life_span") val lifeSpan: String?,
    val temperament: String?,
    val origin: String?,
    @SerializedName("reference_image_id") val referenceImageId: String?,
    val weight: Weight,
    val height: Height
) : Serializable {
    data class Weight(
        val imperial: String,
        val metric: String
    ) : Serializable

    data class Height(
        val imperial: String,
        val metric: String
    ) : Serializable
}
