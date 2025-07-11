// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.mak.feastit.database.entity.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.mak.feastit.database.entity.IngredientEntity
import com.mak.feastit.database.entity.RecipeEntity
import com.mak.feastit.database.entity.ShoppingEntity

data class CartIngredientEntity(
  @Embedded
  val ingredient: IngredientEntity,
  @Relation(
    parentColumn = "id",
    entityColumn = "id",
  )
  val cart: ShoppingEntity,
)

data class RecipeWithIngredient(
  @Embedded
  val recipe: RecipeEntity,
  @Relation(
    parentColumn = "id",
    entityColumn = "recipe_id",
  )
  val ingredients: List<CartIngredientEntity>,
)
