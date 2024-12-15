package com.mak.feastit.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.withTransaction
import com.mak.feastit.database.converters.InstantConverter
import com.mak.feastit.database.dao.LastSyncDAO
import com.mak.feastit.database.dao.PopularRecipeDAO
import com.mak.feastit.database.dao.RecipeDAO
import com.mak.feastit.database.dao.TopRecipesDAO
import com.mak.feastit.database.entity.IngredientEntity
import com.mak.feastit.database.entity.InstructionEntity
import com.mak.feastit.database.entity.LastSyncEntity
import com.mak.feastit.database.entity.PopularRecipeEntity
import com.mak.feastit.database.entity.RecipeEntity
import com.mak.feastit.database.entity.TopRecipeEntity

@Database(
    entities = [
        PopularRecipeEntity::class,
        TopRecipeEntity::class,
        RecipeEntity::class,
        IngredientEntity::class,
        InstructionEntity::class,
        LastSyncEntity::class
    ],
    version = 1
)
@TypeConverters(InstantConverter::class)
internal abstract class FeastDatabase: RoomDatabase(), FeastDB {

    override suspend fun blockTransaction(block: suspend () -> Unit) {
        this.withTransaction {
            block()
        }
    }
}

interface FeastDB {
    fun recipeDAO(): RecipeDAO
    fun popularRecipeDAO(): PopularRecipeDAO
    fun topRecipesDAO(): TopRecipesDAO
    fun lastSyncDao(): LastSyncDAO

    suspend fun blockTransaction(block: suspend () -> Unit)
}
