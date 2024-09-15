package com.ak.feastit.domain.mapper

import com.mak.feastit.database.entity.IngredientEntity
import com.mak.feastit.database.entity.InstructionEntity
import com.mak.feastit.database.entity.RecipeEntity
import com.mak.feastit.database.relations.RecipeDetailEntity
import com.ak.feastit.domain.base.DataMapper
import com.ak.feastit.domain.model.Ingredient
import com.ak.feastit.domain.model.RecipeDetail
import com.ak.feastit.domain.model.Instruction
import com.ak.feastit.domain.model.Recipe

class RecipeDomainMapper: DataMapper<com.mak.feastit.database.entity.RecipeEntity, Recipe>{

    override fun mapToDomainModel(model: com.mak.feastit.database.entity.RecipeEntity): Recipe = Recipe(
            id = model.id,
            recipeName = model.recipeName,
            recipeImgUrl = model.recipeImg
    )

    fun toRecipeDetailDomain(detail: com.mak.feastit.database.relations.RecipeDetailEntity): RecipeDetail {
        val recipe = detail.recipe
        return RecipeDetail(
                recipeId = recipe.id,
                recipeName = recipe.recipeName,
                recipeSummary = recipe.recipeSummary,
                recipeImg = recipe.recipeImg,
                recipeSource = recipe.recipeSource,
                recipeReadyInMins = recipe.recipeReadyInMins,
                servings = recipe.servings,
                pricePerServing = recipe.pricePerServing,
                sourceName = recipe.sourceName,
                isAdded = recipe.isAdded,
                ingredients = toIngredientsDomain(detail.ingredients),
                instructions = toInstructionsDomain(detail.instructions)
        )
    }

    fun toRecipesDomain(recipes: List<com.mak.feastit.database.entity.RecipeEntity>): List<Recipe> = recipes.map { recipe-> mapToDomainModel(recipe) }

    fun toInstructionsDomain(instructions: List<com.mak.feastit.database.entity.InstructionEntity>): List<Instruction> = instructions.map { inst ->
        Instruction(
                stepNo = "Step ${inst.stepNo}",
                stepDesc = inst.stepDesc
        ) }

    fun toIngredientsDomain(ingredients: List<com.mak.feastit.database.entity.IngredientEntity>): List<Ingredient> = ingredients.map { ing ->
        Ingredient(
                id = ing.ingredientUnique,
                image = ing.ingredientImg,
                localizedName = ing.ingredientName,
                name = ing.ingredientSpec
        ) }

}