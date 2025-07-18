// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.mak.feastit.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.withTransaction
import com.mak.feastit.database.converters.InstantConverter
import com.mak.feastit.database.converters.MapStringConverters
import com.mak.feastit.database.dao.HealthyRecipeDAO
import com.mak.feastit.database.dao.IngredientDAO
import com.mak.feastit.database.dao.LastSyncDAO
import com.mak.feastit.database.dao.MealPlannerDAO
import com.mak.feastit.database.dao.NutrientDAO
import com.mak.feastit.database.dao.PocketFriendlyRecipeDAO
import com.mak.feastit.database.dao.PopularRecipeDAO
import com.mak.feastit.database.dao.QuickRecipeDAO
import com.mak.feastit.database.dao.RecipeDAO
import com.mak.feastit.database.dao.RecipeStepDAO
import com.mak.feastit.database.dao.ShoppingDAO
import com.mak.feastit.database.dao.SimilarRecipeDAO
import com.mak.feastit.database.dao.TopRecipesDAO
import com.mak.feastit.database.entity.HealthyRecipeEntity
import com.mak.feastit.database.entity.IngredientEntity
import com.mak.feastit.database.entity.LastSyncEntity
import com.mak.feastit.database.entity.MealPlanEntity
import com.mak.feastit.database.entity.NutrientEntity
import com.mak.feastit.database.entity.PocketFriendlyRecipeEntity
import com.mak.feastit.database.entity.PopularRecipeEntity
import com.mak.feastit.database.entity.QuickRecipeEntity
import com.mak.feastit.database.entity.RecipeEntity
import com.mak.feastit.database.entity.RecipeStepEntity
import com.mak.feastit.database.entity.ShoppingEntity
import com.mak.feastit.database.entity.SimilarRecipeEntity
import com.mak.feastit.database.entity.TopRecipeEntity
import com.mak.feastit.database.migrations.DatabaseMigrationSpec2To3

@Database(
  entities = [
    PopularRecipeEntity::class,
    TopRecipeEntity::class,
    HealthyRecipeEntity::class,
    QuickRecipeEntity::class,
    PocketFriendlyRecipeEntity::class,
    SimilarRecipeEntity::class,
    RecipeEntity::class,
    IngredientEntity::class,
    RecipeStepEntity::class,
    NutrientEntity::class,
    LastSyncEntity::class,
    ShoppingEntity::class,
    MealPlanEntity::class,
  ],
  autoMigrations = [
    AutoMigration(from = 2, to = 3, spec = DatabaseMigrationSpec2To3::class),
  ],
  version = 3,
)
@TypeConverters(InstantConverter::class, MapStringConverters::class)
internal abstract class FeastDatabase :
  RoomDatabase(),
  FeastDB {
  override suspend fun handleTransaction(block: suspend () -> Unit) {
    this.withTransaction {
      block()
    }
  }
}

interface FeastDB {
  fun recipeDAO(): RecipeDAO

  fun popularRecipeDAO(): PopularRecipeDAO

  fun topRecipesDAO(): TopRecipesDAO

  fun healthyRecipeDAO(): HealthyRecipeDAO

  fun quickRecipeDAO(): QuickRecipeDAO

  fun pocketFriendlyRecipeDAO(): PocketFriendlyRecipeDAO

  fun similarRecipeDao(): SimilarRecipeDAO

  fun lastSyncDao(): LastSyncDAO

  fun ingredientDAO(): IngredientDAO

  fun recipeStepDAO(): RecipeStepDAO

  fun nutrientDAO(): NutrientDAO

  fun shoppingDAO(): ShoppingDAO

  fun mealPlanDAO(): MealPlannerDAO

  suspend fun handleTransaction(block: suspend () -> Unit)
}
