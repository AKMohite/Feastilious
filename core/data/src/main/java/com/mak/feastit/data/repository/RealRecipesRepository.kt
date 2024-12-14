package com.mak.feastit.data.repository

import com.mak.feastit.data.mapper.RecipesMapper
import com.mak.feastit.database.FeastDB
import com.mak.feastit.database.entity.RecipeEntity
import com.mak.feastit.domain.model.Recipe
import com.mak.feastit.domain.repository.RecipesRepository
import com.mak.feastit.remote.FeastAPIService

class RealRecipesRepository(
    private val api: FeastAPIService,
    private val db: FeastDB
): RecipesRepository {

    private val recipesMapper = RecipesMapper()

    override suspend fun fetchRecipeFor(params: Int): List<Recipe> {
        val dtos = api.searchRecipes(params).results ?: emptyList()
        val entities = recipesMapper.jsonToEntities(dtos)
        db.blockTransaction {
            chunkUpdate(entities)
        }
        return recipesMapper.entitiesToModels(entities)
    }

    private suspend fun chunkUpdate(entities: List<RecipeEntity>) {
        for (chunk in entities.chunked(20)) {
            db.recipeDAO().insertRecipes(chunk)
        }
    }
}