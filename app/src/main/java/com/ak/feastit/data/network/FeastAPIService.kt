package com.ak.feastit.data.network

import com.ak.feastit.data.network.dto.ComplexSearchDTO
import com.ak.feastit.utils.API_COMPLEX_SEARCH_RECIPES
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface FeastAPIService {

    @GET(API_COMPLEX_SEARCH_RECIPES)
    suspend fun searchRecipes(
            @QueryMap searchQuery: Map<String, String>
    ): ComplexSearchDTO

    /*@GET(API_GET_RECIPE_ANALYSED_INSTRUCTION)
    suspend fun getAnalysedDetail(
            @Path(API_PATH_ID) recipeId: String
    ): List<RecipeDetailDTO>*/
}