// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.mak.feastit.remote

import com.mak.feastit.remote.dto.AnalyzedInstructionDTO
import com.mak.feastit.remote.dto.ComplexSearchDTO
import com.mak.feastit.remote.dto.RecipeDTO
import com.mak.feastit.remote.dto.RecipeInformationDTO
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface FeastAPIService {
  @GET(API_COMPLEX_SEARCH_RECIPES)
  suspend fun searchRecipes(
    @QueryMap searchQuery: Map<String, String>,
  ): ComplexSearchDTO

  @GET(API_GET_RECIPE_DETAIL)
  suspend fun getRecipe(
    @Path(API_PATH_ID) recipeId: Long,
    @QueryMap recipeQuery: Map<String, String>,
  ): RecipeInformationDTO

  @GET(API_GET_RECIPE_ANALYSED_INSTRUCTION)
  suspend fun getAnalyzedInstructions(
    @Path(API_PATH_ID) recipeId: Long,
    @QueryMap query: Map<String, String>,
  ): List<AnalyzedInstructionDTO>

  @GET(API_GET_SIMILAR_RECIPES)
  suspend fun getSimilarRecipes(
    @Path(API_PATH_ID) recipeId: Long,
    @QueryMap query: Map<String, String>,
  ): List<RecipeDTO>

    /*@GET(API_GET_RECIPE_ANALYSED_INSTRUCTION)
    suspend fun getAnalysedDetail(
            @Path(API_PATH_ID) recipeId: String
    ): List<RecipeDetailDTO>*/
}
