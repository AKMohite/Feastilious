package com.mak.feastit.remote.dto


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AnalyzeImageDTO(
//  @Json(name = "category")
//    val category: CategoryDTO? = null,
//  @Json(name = "nutrition")
//    val nutrition: Nutrition? = null,
    @Json(name = "recipes")
    val recipes: List<ImageRecipeDTO>? = null,
//  @Json(name = "status")
//    val status: String? = null
) {
    /*@JsonClass(generateAdapter = true)
    data class CategoryDTO(
        @Json(name = "name")
        val name: String? = null,
        @Json(name = "probability")
        val probability: Double? = null
    )

    @JsonClass(generateAdapter = true)
    data class Nutrition(
        @Json(name = "calories")
        val calories: Calories? = null,
        @Json(name = "carbs")
        val carbs: Carbs? = null,
        @Json(name = "fat")
        val fat: Fat? = null,
        @Json(name = "protein")
        val protein: Protein? = null,
        @Json(name = "recipesUsed")
        val recipesUsed: Int? = null
    ) {
        @JsonClass(generateAdapter = true)
        data class Calories(
            @Json(name = "confidenceRange95Percent")
            val confidenceRange95Percent: ConfidenceRange95Percent? = null,
            @Json(name = "standardDeviation")
            val standardDeviation: Double? = null,
            @Json(name = "unit")
            val unit: String? = null,
            @Json(name = "value")
            val value: Double? = null
        ) {
            @JsonClass(generateAdapter = true)
            data class ConfidenceRange95Percent(
                @Json(name = "max")
                val max: Double? = null,
                @Json(name = "min")
                val min: Double? = null
            )
        }

        @JsonClass(generateAdapter = true)
        data class Carbs(
            @Json(name = "confidenceRange95Percent")
            val confidenceRange95Percent: ConfidenceRange95Percent? = null,
            @Json(name = "standardDeviation")
            val standardDeviation: Double? = null,
            @Json(name = "unit")
            val unit: String? = null,
            @Json(name = "value")
            val value: Double? = null
        ) {
            @JsonClass(generateAdapter = true)
            data class ConfidenceRange95Percent(
                @Json(name = "max")
                val max: Double? = null,
                @Json(name = "min")
                val min: Double? = null
            )
        }

        @JsonClass(generateAdapter = true)
        data class Fat(
            @Json(name = "confidenceRange95Percent")
            val confidenceRange95Percent: ConfidenceRange95Percent? = null,
            @Json(name = "standardDeviation")
            val standardDeviation: Double? = null,
            @Json(name = "unit")
            val unit: String? = null,
            @Json(name = "value")
            val value: Double? = null
        ) {
            @JsonClass(generateAdapter = true)
            data class ConfidenceRange95Percent(
                @Json(name = "max")
                val max: Double? = null,
                @Json(name = "min")
                val min: Double? = null
            )
        }

        @JsonClass(generateAdapter = true)
        data class Protein(
            @Json(name = "confidenceRange95Percent")
            val confidenceRange95Percent: ConfidenceRange95Percent? = null,
            @Json(name = "standardDeviation")
            val standardDeviation: Double? = null,
            @Json(name = "unit")
            val unit: String? = null,
            @Json(name = "value")
            val value: Double? = null
        ) {
            @JsonClass(generateAdapter = true)
            data class ConfidenceRange95Percent(
                @Json(name = "max")
                val max: Double? = null,
                @Json(name = "min")
                val min: Double? = null
            )
        }
    }*/

    @JsonClass(generateAdapter = true)
    data class ImageRecipeDTO(
        @Json(name = "id")
        val id: Long? = null,
        @Json(name = "imageType")
        val imageType: String? = null,
        @Json(name = "title")
        val title: String? = null,
        @Json(name = "url")
        val url: String? = null
    )
}
