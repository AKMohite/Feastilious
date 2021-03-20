package com.ak.feastit.domain.recipelist

import android.util.Log
import androidx.room.withTransaction
import com.ak.feastit.data.local.FeastDatabase
import com.ak.feastit.data.local.IngredientEntity
import com.ak.feastit.data.local.InstructionEntity
import com.ak.feastit.data.local.RecipeEntity
import com.ak.feastit.data.local.base.RecipeDomainMapper
import com.ak.feastit.data.network.FeastAPIService
import com.ak.feastit.data.network.base.RecipeEntityMapper
import com.ak.feastit.domain.utils.RecipeResult
import com.ak.feastit.utils.APP_TAG
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RecipeRepositoryImpl constructor(
    private val apiService: FeastAPIService,
    private val feastDatabase: FeastDatabase,
    private val recipeEntityMapper: RecipeEntityMapper,
    private val recipeDomainMapper: RecipeDomainMapper,
) : RecipeRepository {

    private val recipeDAO = feastDatabase.recipeDAO()

    override fun searchRecipes(params: HashMap<String, String>): Flow<RecipeResult<List<Recipe>>> = flow {
        try {
            emit(RecipeResult.loading())
            val response = apiService.searchRecipes(params)
            var recipes = listOf<Recipe>()

            val apiRecipes = response.results
            if (!apiRecipes.isNullOrEmpty()) {

                val recipeDetails = recipeEntityMapper.toEntityList(apiRecipes)

                //            TODO handle favorite recipes too and null assertion

                if (!recipeDetails.isNullOrEmpty()) {
                    val dtoRecipes: MutableList<RecipeEntity> = mutableListOf()
                    val ingredients: MutableList<IngredientEntity> = mutableListOf()
                    val instructions: MutableList<InstructionEntity> = mutableListOf()
                    recipeDetails.forEach { recipe ->
                        dtoRecipes.add(recipe.recipe)
                        ingredients.addAll(recipe.ingredients)
                        instructions.addAll(recipe.instructions)
                    }
                    feastDatabase.withTransaction {
                        recipeDAO.insertRecipes(dtoRecipes)
                        recipeDAO.insertIngredients(ingredients)
                        recipeDAO.insertInstructions(instructions)
                        recipeDAO.deleteRecipes()
                    }
                }
            }

            val recipeDetails = recipeDAO.getRecipeDetail(716426)

            val recipeSingle = recipeDetails.recipe

            val localRecipes = recipeDAO.getRecipes()
            recipes = recipeDomainMapper.toRecipesDomain(localRecipes)


            emit(RecipeResult.success(recipes))
        } catch (e: Exception) {
            emit(RecipeResult.error(e.message ?: "An error occurred"))
            Log.e(APP_TAG, "getRandomRecipes: ${e.message}")
        }
    }
}