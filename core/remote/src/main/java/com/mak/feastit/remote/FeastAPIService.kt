// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.mak.feastit.remote

import com.mak.feastit.remote.dto.AnalyzeImageDTO
import com.mak.feastit.remote.dto.AnalyzedInstructionDTO
import com.mak.feastit.remote.dto.ComplexSearchDTO
import com.mak.feastit.remote.dto.RecipeDTO
import com.mak.feastit.remote.dto.RecipeInformationDTO
import java.io.File
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
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

  suspend fun searchRecipesByImage(file: File): List<RecipeDTO> {
    val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
    val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
//    val response = analyzeImageForRecipes(body)
//    Log.d("FeastAPIService", "$response")
//    Log.d("FeastAPIService", "${response.body}")
    return emptyList()
  }

  suspend fun searchRecipesByBytes(bytes: ByteArray, type: String?): AnalyzeImageDTO {
    val requestFile = bytes.toRequestBody(
      type?.toMediaTypeOrNull(),
      0, bytes.size
    )
    val body = MultipartBody.Part.createFormData("file", "image.jpg", requestFile)
    val response = analyzeImageForRecipes(body)
//    Log.d("FeastAPIService", "$response")
//    Log.d("FeastAPIService", "${response.body}")
    return response
  }

  @Multipart
  @POST(API_ANALYZE_IMAGE)
  suspend fun analyzeImageForRecipes(
    @Part image: MultipartBody.Part
  ): AnalyzeImageDTO

  /*@GET(API_GET_RECIPE_ANALYSED_INSTRUCTION)
  suspend fun getAnalysedDetail(
          @Path(API_PATH_ID) recipeId: String
  ): List<RecipeDetailDTO>*/
}
