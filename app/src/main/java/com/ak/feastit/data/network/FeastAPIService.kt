package com.ak.feastit.data.network

import com.ak.feastit.data.network.model.RecipesDTO
import com.ak.feastit.utils.API_GET_RANDOM_RECIPES
import retrofit2.http.GET
import retrofit2.http.Query

interface FeastAPIService {

    @GET(API_GET_RANDOM_RECIPES)
    suspend fun getRandomRecipes(
        @Query("number") pageSize: Int = 30
    ): RecipesDTO
}