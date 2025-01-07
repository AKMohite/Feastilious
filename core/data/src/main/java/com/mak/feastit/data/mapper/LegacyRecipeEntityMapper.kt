package com.mak.feastit.data.mapper

import com.mak.feastit.domain.model.IMG_INGREDIENT_BASE_URL
import com.mak.feastit.remote.dto.AnalyzedInstructionDTO
import com.mak.feastit.remote.dto.ExtendedIngredientDTO
import com.mak.feastit.remote.dto.RecipeDTO
import java.util.Locale

internal class LegacyRecipeEntityMapper:
    com.mak.feastit.data.base.DataMapper<RecipeDTO, com.mak.feastit.database.relations.RecipeDetailEntity> {

    override fun mapToDomainModel(model: RecipeDTO): com.mak.feastit.database.relations.RecipeDetailEntity {
        return com.mak.feastit.database.relations.RecipeDetailEntity(
            recipe = com.mak.feastit.database.entity.RecipeEntity(
                id = model.id,
                recipeName = model.title,
                recipeSummary = model.summary ?: "",
                recipeImg = "model.image",
                recipeSource = model.sourceUrl ?: "",
                recipeReadyInMins = model.readyInMinutes ?: 0,
                servings = model.servings ?: 0,
                pricePerServing = model.pricePerServing ?: 0.0,
                sourceName = model.sourceName ?: "",
                isAddedToCollection = false,
                cuisines = model.cuisines?.joinToString(",") ?: "",
                dishTypes = model.dishTypes?.joinToString(",") ?: "",
                diets = model.diets?.joinToString(",") ?: "",
                caloricBreakdown = emptyMap()
            ),
            instructions = toInstructionsEntity(model.id, model.analyzedInstructions),
            ingredients = toIngredientsEntity(model.id, model.extendedIngredients)
        )
    }

    private fun toIngredientsEntity(recipeId: Long, ingredients: List<ExtendedIngredientDTO>?): List<com.mak.feastit.database.entity.IngredientEntity> {
        return ingredients?.map { ing ->
            com.mak.feastit.database.entity.IngredientEntity(
                recipeId = recipeId,
                id = "0",
                ingredientId = throw Exception("Delete class No usage"),
                ingredientImg = if (!ing.image.isNullOrBlank()) "$IMG_INGREDIENT_BASE_URL${ing.image}" else "",
                ingredientName = ing.name?.capitalize(Locale.getDefault()) ?: "",
                quantity = ing.amount ?: 0.0,
                unit = ing.unit ?: ""
            )
        } ?: emptyList()
    }

    private fun toInstructionsEntity(recipeId: Long, instructions: List<AnalyzedInstructionDTO>?): List<com.mak.feastit.database.entity.RecipeStepEntity> {
        val recipeSteps = if (!instructions.isNullOrEmpty()) {
            instructions[0].steps
        } else {
            emptyList()
        }
        return recipeSteps?.map { inst ->
            com.mak.feastit.database.entity.RecipeStepEntity(
                recipeId = recipeId,
                stepId = "$recipeId-${inst.number ?: 0}",
                stepNo = inst.number ?: 0,
                stepDescription = "inst.step",
                stepName = ""
            )
        } ?: emptyList()
    }

    fun toEntityList(apiRecipes: List<RecipeDTO>): List<com.mak.feastit.database.relations.RecipeDetailEntity> {
        return apiRecipes.map { recipe->  mapToDomainModel(recipe) }
    }
}