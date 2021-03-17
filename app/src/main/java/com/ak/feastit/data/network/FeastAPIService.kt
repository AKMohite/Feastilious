package com.ak.feastit.data.network

import com.ak.feastit.data.network.model.RecipeDetailDTO
import com.ak.feastit.data.network.model.RecipesDTO
import com.ak.feastit.utils.API_GET_RANDOM_RECIPES
import com.ak.feastit.utils.API_GET_RECIPE_ANALYSED_INSTRUCTION
import com.ak.feastit.utils.API_PATH_ID
import com.ak.feastit.utils.DEFAULT_PAGESIZE
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FeastAPIService {

    @GET(API_GET_RANDOM_RECIPES)
    suspend fun getRandomRecipes(
        @Query("number") pageSize: Int = DEFAULT_PAGESIZE
    ): RecipesDTO

    @GET(API_GET_RECIPE_ANALYSED_INSTRUCTION)
    suspend fun getAnalysedDetail(
            @Path(API_PATH_ID) recipeId: String
    ): List<RecipeDetailDTO>
}