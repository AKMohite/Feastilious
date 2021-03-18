package com.ak.feastit.data.network

import com.ak.feastit.data.network.model.ComplexSearchDTO
import com.ak.feastit.data.network.model.RecipeDetailDTO
import com.ak.feastit.data.network.model.RecipesDTO
import com.ak.feastit.utils.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface FeastAPIService {

    @GET(API_COMPLEX_SEARCH_RECIPES)
    suspend fun searchRecipes(
            @QueryMap searchQuery: Map<String, String>
    ): ComplexSearchDTO

    @GET(API_GET_RECIPE_ANALYSED_INSTRUCTION)
    suspend fun getAnalysedDetail(
            @Path(API_PATH_ID) recipeId: String
    ): List<RecipeDetailDTO>
}