package com.mak.feastit.data.mapper

import com.mak.feastit.database.entity.PopularRecipeEntity
import com.mak.feastit.database.entity.RecipeEntity
import com.mak.feastit.domain.model.Recipe
import com.mak.feastit.remote.dto.RecipeDTO

internal class RecipesMapper: BaseMapper<RecipeDTO, RecipeEntity, Recipe>() {

    override fun jsonToEntity(json: RecipeDTO): RecipeEntity {
        return RecipeEntity(
                id = json.id,
                recipeName = json.title,
                recipeSummary = json.summary ?: "",
                recipeImg = json.image,
                recipeSource = json.sourceUrl ?: "",
                recipeReadyInMins = json.readyInMinutes ?: 0,
                servings = json.servings ?: 0,
                pricePerServing = json.pricePerServing ?: 0.0,
                sourceName = json.sourceName ?: "",
                isAdded = false,
                cuisines = json.cuisines?.joinToString(",") ?: "",
                dishTypes = json.dishTypes?.joinToString(",") ?: "",
                diets = json.diets?.joinToString(",") ?: ""
            )
    }

    override fun entityToModel(entity: RecipeEntity): Recipe {
        return Recipe(
            id = entity.id,
            recipeName = entity.recipeName,
            recipeImgUrl = entity.recipeImg
        )
    }

    fun jsonToPopularEntities(dtos: List<RecipeDTO>, page: Int): List<PopularRecipeEntity> {
        return dtos.map { dto ->
            PopularRecipeEntity(
                recipeId = dto.id,
                page = page
            )
        }
    }
}