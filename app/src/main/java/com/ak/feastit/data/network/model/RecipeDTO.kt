package com.ak.feastit.data.network.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RecipeDTO(
    val aggregateLikes: Int?,
    val analyzedInstructions: List<AnalyzedInstructionDTO?>?,
    val cheap: Boolean?,
    val creditsText: String?,
    val cuisines: List<Any?>?,
    val dairyFree: Boolean?,
    val diets: List<String?>?,
    val dishTypes: List<String?>?,
    val extendedIngredients: List<ExtendedIngredientDTO?>?,
    val gaps: String?,
    val glutenFree: Boolean?,
    val healthScore: Double?,
    val id: Long = 0,
    val image: String,
    val imageType: String?,
    val instructions: String?,
    val license: String?,
    val lowFodmap: Boolean?,
    val occasions: List<String?>?,
    val originalId: Any?,
    val pricePerServing: Double?,
    val readyInMinutes: Int?,
    val servings: Int?,
    val sourceName: String?,
    val sourceUrl: String?,
    val spoonacularScore: Double?,
    val spoonacularSourceUrl: String?,
    val summary: String?,
    val sustainable: Boolean?,
    val title: String = "",
    val vegan: Boolean?,
    val vegetarian: Boolean?,
    val veryHealthy: Boolean?,
    val veryPopular: Boolean?,
    val weightWatcherSmartPoints: Int?
)