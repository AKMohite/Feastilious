// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.mak.feastit.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.mak.feastit.database.entity.IngredientEntity
import com.mak.feastit.database.entity.RecipeEntity
import com.mak.feastit.database.entity.RecipeStepEntity

data class RecipeDetailEntity(
  @Embedded
  val recipe: RecipeEntity,
  @Relation(
    parentColumn = "id",
    entityColumn = "recipe_id",
  )
  val ingredients: List<IngredientEntity>,
  @Relation(
    parentColumn = "id",
    entityColumn = "recipe_id",
  )
  val instructions: List<RecipeStepEntity>,
)
