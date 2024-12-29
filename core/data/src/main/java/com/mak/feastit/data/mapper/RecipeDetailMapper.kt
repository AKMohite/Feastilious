package com.mak.feastit.data.mapper

import com.mak.feastit.database.entity.IngredientEntity
import com.mak.feastit.database.entity.RecipeEntity
import com.mak.feastit.domain.model.RecipeDetail
import com.mak.feastit.remote.dto.RecipeInformationDTO
import com.mak.feastit.remote.dto.RecipeIngredientDTO
import javax.inject.Inject

internal class RecipeDetailMapper @Inject constructor(): BaseMapper<RecipeInformationDTO, RecipeEntity, RecipeDetail>() {

    override fun jsonToEntity(json: RecipeInformationDTO): RecipeEntity {
        return RecipeEntity(
            id = json.id,
            recipeName = json.title.orEmpty(),
            recipeSummary = json.summary.orEmpty(),
            recipeImg = json.image.orEmpty(),
            recipeReadyInMins = json.readyInMinutes ?: 0,
            servings = json.servings ?: 1,
            pricePerServing = json.pricePerServing ?: 0.0,
            sourceName = json.sourceName.orEmpty(),
            recipeSource = json.sourceUrl.orEmpty(),
            isAdded = false,
            cuisines = get(json.cuisines),
            dishTypes = get(json.dishTypes),
            diets = get(json.diets)
        )
    }

    override fun entityToModel(entity: RecipeEntity): RecipeDetail {
        return RecipeDetail(
            recipeId = entity.id,
            recipeName = entity.recipeName,
            recipeSummary = entity.recipeSummary,
            recipeImg = entity.recipeImg,
            recipeSource = entity.recipeSource,
            recipeReadyInMins = entity.recipeReadyInMins,
            servings = entity.servings,
            pricePerServing = entity.pricePerServing,
            sourceName = entity.sourceName,
            isAdded = entity.isAdded,
//            ingredients = toIngredientsDomain(detail.ingredients),
//            instructions = toInstructionsDomain(detail.instructions)
        )
    }

    private fun get(cuisines: List<String>?): String {
        return cuisines?.joinToString(separator = ",") ?: ""
    }

    fun jsonToIngredientsEntity(recipeId: Long, ingredients: List<RecipeIngredientDTO>?): List<IngredientEntity> {
        return ingredients?.map { dto ->
            IngredientEntity(
                ingredientUnique = "${recipeId}_${dto.id}",
                aisle = dto.aisle ?: "No category",
                recipeId = recipeId,
                ingredientName = dto.name.orEmpty(),
                amount = dto.amount ?: 0.0,
                unit = dto.unit.orEmpty(),
                ingredientImg = dto.image.orEmpty()
            )
        } ?: return emptyList()
    }
}