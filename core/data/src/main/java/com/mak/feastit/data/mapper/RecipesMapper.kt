// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.mak.feastit.data.mapper

import com.mak.feastit.database.entity.HealthyRecipeEntity
import com.mak.feastit.database.entity.PocketFriendlyRecipeEntity
import com.mak.feastit.database.entity.PopularRecipeEntity
import com.mak.feastit.database.entity.QuickRecipeEntity
import com.mak.feastit.database.entity.RecipeEntity
import com.mak.feastit.database.entity.TopRecipeEntity
import com.mak.feastit.database.entity.custom.PaginatedRecipeEntity
import com.mak.feastit.domain.model.ImageType
import com.mak.feastit.domain.model.Recipe
import com.mak.feastit.domain.model.RecipeImage
import com.mak.feastit.domain.model.RecipeImageSize
import com.mak.feastit.remote.dto.RecipeDTO

internal class RecipesMapper : BaseMapper<RecipeDTO, RecipeEntity, Recipe>() {
  override fun jsonToEntity(json: RecipeDTO): RecipeEntity = RecipeEntity(
    id = json.id,
    recipeName = json.title,
    recipeSummary = json.summary ?: "",
    recipeSource = json.sourceUrl ?: "",
    recipeReadyInMins = json.readyInMinutes ?: 0,
    servings = json.servings ?: 0,
    pricePerServing = json.pricePerServing ?: 0.0,
    sourceName = json.sourceName ?: "",
    isAddedToCollection = false,
    extension = (json.imageType ?: "").ifBlank { "jpg" },
    cuisines = json.cuisines?.joinToString(",") ?: "",
    dishTypes = json.dishTypes?.joinToString(",") ?: "",
    diets = json.diets?.joinToString(",") ?: "",
    caloricBreakdown = emptyMap(),
  )

  override fun entityToModel(entity: RecipeEntity): Recipe = Recipe(
    id = entity.id,
    name = entity.recipeName,
    image = RecipeImage(entity.id, entity.extension, RecipeImageSize.MEDIUM, ImageType.CELL),
    page = 1, // TODO handle page number
  )

  fun paginatedEntityToModel(entity: PaginatedRecipeEntity): Recipe = Recipe(
    id = entity.id,
    name = entity.recipeName,
    image = RecipeImage(entity.id, entity.extension, RecipeImageSize.MEDIUM, ImageType.CELL),
    page = entity.page,
  )

  fun jsonToPopularEntities(
    dtos: List<RecipeDTO>,
    page: Int,
  ): List<PopularRecipeEntity> = dtos.map { dto ->
    PopularRecipeEntity(
      recipeId = dto.id,
      page = page,
    )
  }

  fun jsonToTopEntities(
    dtos: List<RecipeDTO>,
    page: Int,
  ): List<TopRecipeEntity> = dtos.map { dto ->
    TopRecipeEntity(
      recipeId = dto.id,
      page = page,
    )
  }

  fun jsonToHealthyEntities(
    dtos: List<RecipeDTO>,
    page: Int,
  ): List<HealthyRecipeEntity> = dtos.map { dto ->
    HealthyRecipeEntity(
      recipeId = dto.id,
      page = page,
    )
  }

  fun jsonToQuickEntities(
    dtos: List<RecipeDTO>,
    page: Int,
  ): List<QuickRecipeEntity> = dtos.map { dto ->
    QuickRecipeEntity(
      recipeId = dto.id,
      page = page,
    )
  }

  fun jsonToPocketFriendlyEntities(
    dtos: List<RecipeDTO>,
    page: Int,
  ): List<PocketFriendlyRecipeEntity> = dtos.map { dto ->
    PocketFriendlyRecipeEntity(
      recipeId = dto.id,
      page = page,
    )
  }
}
