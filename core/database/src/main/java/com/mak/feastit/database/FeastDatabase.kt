package com.mak.feastit.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.withTransaction
import com.mak.feastit.database.converters.InstantConverter
import com.mak.feastit.database.dao.HealthyRecipeDAO
import com.mak.feastit.database.dao.LastSyncDAO
import com.mak.feastit.database.dao.PocketFriendlyRecipeDAO
import com.mak.feastit.database.dao.PopularRecipeDAO
import com.mak.feastit.database.dao.QuickRecipeDAO
import com.mak.feastit.database.dao.RecipeDAO
import com.mak.feastit.database.dao.TopRecipesDAO
import com.mak.feastit.database.entity.HealthyRecipeEntity
import com.mak.feastit.database.entity.IngredientEntity
import com.mak.feastit.database.entity.RecipeStepEntity
import com.mak.feastit.database.entity.LastSyncEntity
import com.mak.feastit.database.entity.PocketFriendlyRecipeEntity
import com.mak.feastit.database.entity.PopularRecipeEntity
import com.mak.feastit.database.entity.QuickRecipeEntity
import com.mak.feastit.database.entity.RecipeEntity
import com.mak.feastit.database.entity.TopRecipeEntity

@Database(
    entities = [
        PopularRecipeEntity::class,
        TopRecipeEntity::class,
        HealthyRecipeEntity::class,
        QuickRecipeEntity::class,
        PocketFriendlyRecipeEntity::class,
        RecipeEntity::class,
        IngredientEntity::class,
        RecipeStepEntity::class,
        LastSyncEntity::class
    ],
    version = 1
)
@TypeConverters(InstantConverter::class)
internal abstract class FeastDatabase: RoomDatabase(), FeastDB {

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
    fun lastSyncDao(): LastSyncDAO

    suspend fun handleTransaction(block: suspend () -> Unit)
}
