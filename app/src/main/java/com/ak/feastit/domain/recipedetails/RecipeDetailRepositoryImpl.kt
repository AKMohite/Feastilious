package com.ak.feastit.domain.recipedetails

import android.util.Log
import com.ak.feastit.data.network.FeastAPIService
import com.ak.feastit.domain.recipelist.Recipe
import com.ak.feastit.domain.utils.RecipeResult
import com.ak.feastit.utils.APP_TAG
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RecipeDetailRepositoryImpl constructor(
        private val apiService: FeastAPIService
) : RecipeDetailRepository {

    override fun getRecipeDetail(recipe: Recipe): Flow<RecipeResult<RecipeDetail>> = flow {
        try {
            emit(RecipeResult.loading())
            val detailResponse = apiService.getAnalysedDetail(recipe.id.toString())
            if (!detailResponse.isNullOrEmpty()) {
                val subRecipes = mutableListOf<SubRecipe>()
                var recipeName = recipe.recipeName
                detailResponse.forEachIndexed { detailIndex, recipeDetailDTOItem ->
                    val recipeInstructions: MutableList<RecipeInstruction> = mutableListOf<RecipeInstruction>()
                    val ingredients: MutableList<Ingredient> = mutableListOf<Ingredient>()

                    if (detailIndex != 0) {
                        recipeName = detailResponse[detailIndex].name ?: "Sub Recipe $detailIndex"
                    }
                    recipeDetailDTOItem.steps?.forEachIndexed { stepIndex, stepDTO ->
                        if (stepDTO != null) {
                            stepDTO.ingredients?.forEachIndexed { ingredientIndex, ingredientDTO ->
                                ingredients.add(Ingredient(
                                        id = ingredientDTO?.id!!,
                                        image = ingredientDTO.image!!,
                                        name = ingredientDTO.name!!,
                                        localizedName = ingredientDTO.localizedName!!
                                ))
                            }

                            recipeInstructions.add(RecipeInstruction(
                                    number = stepDTO.number!!,
                                    step = stepDTO.step!!
                            ))
                        }
                    }

//                    Sort steps by number
                    recipeInstructions.sortBy { inst -> inst.number }
//                    Check for duplicate ingredients
//                    ingredients.sortBy { ing -> ing.id }

                    subRecipes.add(SubRecipe(
                            recipeName = recipeName,
                            ingredients = ingredients.distinctBy { ing -> ing.id to ing.name },
                            instructions = recipeInstructions
                    ))
                }


                val recipeDetail = RecipeDetail(
                        id = recipe.id,
                        recipeName = recipe.recipeName,
                        recipeImgUrl = recipe.recipeImgUrl,
                        subRecipes = subRecipes
                )

                emit(RecipeResult.success(recipeDetail))
            } else {
                emit(RecipeResult.error<RecipeDetail>("An error occurred"))
            }
        } catch (error: Exception) {
            emit(RecipeResult.error(error.message ?: "An error occurred"))
            Log.d(APP_TAG, "getRecipeDetail: ${error.message}")
        }
    }
}