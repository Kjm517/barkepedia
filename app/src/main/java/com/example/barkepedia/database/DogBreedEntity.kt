package com.example.barkepedia.database

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.barkepedia.model.DogBreed

@Entity(tableName = "dog_breeds")
data class DogBreedEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    @ColumnInfo(name = "breed_group")
    val breedGroup: String?,
    @ColumnInfo(name = "bred_for")
    val bredFor: String?,
    @ColumnInfo(name = "life_span")
    val lifeSpan: String?,
    val temperament: String?,
    val origin: String?,
    @ColumnInfo(name = "reference_image_id")
    val referenceImageId: String?,
    @Embedded(prefix = "weight_")
    val weight: Weight,
    @Embedded(prefix = "height_")
    val height: Height
) {
    data class Weight(
        val imperial: String,
        val metric: String
    )

    data class Height(
        val imperial: String,
        val metric: String
    )

    fun toDogBreed(): DogBreed {
        return DogBreed(
            id = id,
            name = name,
            breedGroup = breedGroup,
            bredFor = bredFor,
            lifeSpan = lifeSpan,
            temperament = temperament,
            origin = origin,
            referenceImageId = referenceImageId,
            weight = DogBreed.Weight(weight.imperial, weight.metric),
            height = DogBreed.Height(weight.imperial, weight.metric)
        )
    }
}

// Extension function to convert domain model to entity
fun DogBreed.toEntity(): DogBreedEntity {
    return DogBreedEntity(
        id = id,
        name = name,
        breedGroup = breedGroup,
        bredFor = bredFor,
        lifeSpan = lifeSpan,
        temperament = temperament,
        origin = origin,
        referenceImageId = referenceImageId,
        weight = DogBreedEntity.Weight(
            weight.imperial,
            weight.metric
        ),
        height = DogBreedEntity.Height(
            height.imperial,
            height.metric
        )
    )
}