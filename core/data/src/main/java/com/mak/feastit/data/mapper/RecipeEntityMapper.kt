package com.mak.feastit.data.mapper

import com.mak.feastit.data.utils.IMG_INGREDIENT_BASE_URL
import com.mak.feastit.remote.dto.AnalyzedInstructionDTO
import com.mak.feastit.remote.dto.ExtendedIngredientDTO
import com.mak.feastit.remote.dto.RecipeDTO
import java.util.Locale

class RecipeEntityMapper:
    com.mak.feastit.data.base.DataMapper<RecipeDTO, com.mak.feastit.database.relations.RecipeDetailEntity> {

    override fun mapToDomainModel(model: RecipeDTO): com.mak.feastit.database.relations.RecipeDetailEntity {
        return com.mak.feastit.database.relations.RecipeDetailEntity(
            recipe = com.mak.feastit.database.entity.RecipeEntity(
                id = model.id,
                recipeName = model.title,
                recipeSummary = model.summary ?: "",
                recipeImg = model.image,
                recipeSource = model.sourceUrl ?: "",
                recipeReadyInMins = model.readyInMinutes ?: 0,
                servings = model.servings ?: 0,
                pricePerServing = model.pricePerServing ?: 0.0,
                sourceName = model.sourceName ?: "",
                isAdded = false,
                cuisines = model.cuisines?.joinToString(",") ?: "",
                dishTypes = model.dishTypes?.joinToString(",") ?: "",
                diets = model.diets?.joinToString(",") ?: ""
            ),
            instructions = toInstructionsEntity(model.id, model.analyzedInstructions),
            ingredients = toIngredientsEntity(model.id, model.extendedIngredients)
        )
    }

    private fun toIngredientsEntity(recipeId: Long, ingredients: List<ExtendedIngredientDTO>?): List<com.mak.feastit.database.entity.IngredientEntity> {
        return ingredients?.map { ing ->
            com.mak.feastit.database.entity.IngredientEntity(
                recipeId = recipeId,
                ingredientUnique = "$recipeId-${
                    ing.name?.capitalize(Locale.getDefault())?.replace(" ", "") ?: ""
                }",
                ingredientConsistency = ing.consistency ?: "",
                ingredientImg = if (!ing.image.isNullOrBlank()) "$IMG_INGREDIENT_BASE_URL${ing.image}" else "",
                ingredientName = ing.name?.capitalize(Locale.getDefault()) ?: "",
                ingredientSpec = ing.original ?: "",
                amount = ing.amount ?: 0.0,
                unit = ing.unit ?: ""
            )
        } ?: emptyList()
    }

    private fun toInstructionsEntity(recipeId: Long, instructions: List<AnalyzedInstructionDTO>?): List<com.mak.feastit.database.entity.InstructionEntity> {
        val recipeSteps = if (!instructions.isNullOrEmpty()) {
            instructions[0].steps
        } else {
            emptyList()
        }
        return recipeSteps?.map { inst ->
            com.mak.feastit.database.entity.InstructionEntity(
                recipeId = recipeId,
                stepId = "$recipeId-${inst.number ?: 0}",
                stepNo = inst.number ?: 0,
                stepDesc = inst.step,
                stepIngredients = inst.ingredients?.filter { ingred -> !ingred.name.isNullOrEmpty() }
                    ?.joinToString(",") { ingred -> ingred.name ?: "" } ?: "",
                stepEquipments = inst.equipment?.filter { equip -> !equip.name.isNullOrEmpty() }
                    ?.joinToString(",") { equip -> equip.name ?: "" } ?: "",
            )
        } ?: emptyList()
    }

    fun toEntityList(apiRecipes: List<RecipeDTO>): List<com.mak.feastit.database.relations.RecipeDetailEntity> {
        return apiRecipes.map { recipe->  mapToDomainModel(recipe) }
    }
}