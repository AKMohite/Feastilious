package com.mak.feastit.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RecipeInformationDTO(
//    @Json(name = "aggregateLikes")
//    val aggregateLikes: Int? = null,
//    @Json(name = "analyzedInstructions")
//    val analyzedInstructions: List<Any?>? = null,
    @Json(name = "cheap")
    val cheap: Boolean? = null,
    @Json(name = "cookingMinutes")
    val cookingMinutes: Int? = null,
    @Json(name = "creditsText")
    val creditsText: String? = null,
    @Json(name = "cuisines")
    val cuisines: List<String>? = null,
    @Json(name = "dairyFree")
    val dairyFree: Boolean? = null,
    @Json(name = "diets")
    val diets: List<String>? = null,
    @Json(name = "dishTypes")
    val dishTypes: List<String>? = null,
    @Json(name = "extendedIngredients")
    val extendedIngredients: List<RecipeIngredientDTO>? = null,
//    @Json(name = "gaps")
//    val gaps: String? = null,
//    @Json(name = "glutenFree")
//    val glutenFree: Boolean? = null,
    @Json(name = "healthScore")
    val healthScore: Double? = null,
    @Json(name = "id")
    val id: Long,
    @Json(name = "image")
    val image: String? = null,
//    @Json(name = "imageType")
//    val imageType: String? = null,
//    @Json(name = "instructions")
//    val instructions: String? = null,
//    @Json(name = "license")
//    val license: String? = null,
//    @Json(name = "lowFodmap")
//    val lowFodmap: Boolean? = null,
    @Json(name = "nutrition")
    val nutrition: NutritionDTO? = null,
//    @Json(name = "occasions")
//    val occasions: List<Any?>? = null,
//    @Json(name = "originalId")
//    val originalId: Any? = null,
    @Json(name = "preparationMinutes")
    val preparationMinutes: Int? = null,
    @Json(name = "pricePerServing")
    val pricePerServing: Double? = null,
    @Json(name = "readyInMinutes")
    val readyInMinutes: Int? = null,
    @Json(name = "servings")
    val servings: Int? = null,
    @Json(name = "sourceName")
    val sourceName: String? = null,
    @Json(name = "sourceUrl")
    val sourceUrl: String? = null,
//    @Json(name = "spoonacularScore")
//    val spoonacularScore: Double? = null,
//    @Json(name = "spoonacularSourceUrl")
//    val spoonacularSourceUrl: String? = null,
    @Json(name = "summary")
    val summary: String? = null,
//    @Json(name = "sustainable")
//    val sustainable: Boolean? = null,
    @Json(name = "title")
    val title: String? = null,
//    @Json(name = "vegan")
//    val vegan: Boolean? = null,
//    @Json(name = "vegetarian")
//    val vegetarian: Boolean? = null,
//    @Json(name = "veryHealthy")
//    val veryHealthy: Boolean? = null,
//    @Json(name = "veryPopular")
//    val veryPopular: Boolean? = null,
//    @Json(name = "weightWatcherSmartPoints")
//    val weightWatcherSmartPoints: Int? = null
)

@JsonClass(generateAdapter = true)
data class RecipeIngredientDTO(
    @Json(name = "aisle")
    val aisle: String? = null,
    @Json(name = "amount")
    val amount: Double? = null,
//        @Json(name = "consistency")
//        val consistency: String? = null,
    @Json(name = "id")
    val id: Long,
    @Json(name = "image")
    val image: String? = null,
    @Json(name = "measures")
    val measures: RecipeMeasuresDTO? = null,
//        @Json(name = "meta")
//        val meta: List<String?>? = null,
    @Json(name = "name")
    val name: String? = null,
    @Json(name = "nameClean")
    val nameClean: String? = null,
    @Json(name = "original")
    val original: String? = null,
    @Json(name = "originalName")
    val originalName: String? = null,
    @Json(name = "unit")
    val unit: String? = null,
    @Json(name = "nutrients")
    val nutrients: List<NutrientDTO?>? = null,
)

@JsonClass(generateAdapter = true)
data class RecipeMeasuresDTO(
    @Json(name = "metric")
    val metric: MetricMeasureDTO? = null,
    @Json(name = "us")
    val us: USMeasureDTO? = null
)

@JsonClass(generateAdapter = true)
data class MetricMeasureDTO(
    @Json(name = "amount")
    val amount: Double? = null,
    @Json(name = "unitLong")
    val unitLong: String? = null,
    @Json(name = "unitShort")
    val unitShort: String? = null
)

@JsonClass(generateAdapter = true)
data class USMeasureDTO(
    @Json(name = "amount")
    val amount: Double? = null,
    @Json(name = "unitLong")
    val unitLong: String? = null,
    @Json(name = "unitShort")
    val unitShort: String? = null
)

@JsonClass(generateAdapter = true)
data class NutritionDTO(
    @Json(name = "caloricBreakdown")
    val caloricBreakdown: Map<String, Double>? = emptyMap(),
//    @Json(name = "flavonoids")
//    val flavonoids: List<FlavonoidDTO?>? = null,
//    @Json(name = "ingredients")
//    val ingredients: List<RecipeIngredientDTO>? = null,
    @Json(name = "nutrients")
    val nutrients: List<NutrientDTO>? = null,
//    @Json(name = "properties")
//    val properties: List<NutritionPropertyDTO>? = null,
//    @Json(name = "weightPerServing")
//    val weightPerServing: NutritionWeightPerServingDTO? = null
)

@JsonClass(generateAdapter = true)
data class CaloricBreakdownDTO(
    @Json(name = "percentCarbs")
    val percentCarbs: Double? = null,
    @Json(name = "percentFat")
    val percentFat: Double? = null,
    @Json(name = "percentProtein")
    val percentProtein: Double? = null
)

@JsonClass(generateAdapter = true)
data class NutrientDTO(
    @Json(name = "amount")
    val amount: Double? = null,
    @Json(name = "name")
    val name: String? = null,
    @Json(name = "percentOfDailyNeeds")
    val percentOfDailyNeeds: Double? = null,
    @Json(name = "unit")
    val unit: String? = null
)

//@JsonClass(generateAdapter = true)
//data class FlavonoidDTO(
//    @Json(name = "amount")
//    val amount: Double? = null,
//    @Json(name = "name")
//    val name: String? = null,
//    @Json(name = "unit")
//    val unit: String? = null
//)
//
//@JsonClass(generateAdapter = true)
//data class NutritionPropertyDTO(
//    @Json(name = "amount")
//    val amount: Double? = null,
//    @Json(name = "name")
//    val name: String? = null,
//    @Json(name = "unit")
//    val unit: String? = null
//)
//
//@JsonClass(generateAdapter = true)
//data class NutritionWeightPerServingDTO(
//    @Json(name = "amount")
//    val amount: Int? = null,
//    @Json(name = "unit")
//    val unit: String? = null
//)