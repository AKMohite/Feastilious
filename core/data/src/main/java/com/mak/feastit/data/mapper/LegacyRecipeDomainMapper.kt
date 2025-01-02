package com.mak.feastit.data.mapper

import com.mak.feastit.domain.model.Ingredient
import com.mak.feastit.domain.model.Instruction
import com.mak.feastit.domain.model.Recipe
import com.mak.feastit.domain.model.RecipeDetail

internal class LegacyRecipeDomainMapper:
    com.mak.feastit.data.base.DataMapper<com.mak.feastit.database.entity.RecipeEntity, Recipe> {

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
                isAddedToCollection = recipe.isAddedToCollection,
                ingredients = toIngredientsDomain(detail.ingredients),
                instructions = toInstructionsDomain(detail.instructions)
        )
    }

    fun toRecipesDomain(recipes: List<com.mak.feastit.database.entity.RecipeEntity>): List<Recipe> = recipes.map { recipe-> mapToDomainModel(recipe) }

    fun toInstructionsDomain(instructions: List<com.mak.feastit.database.entity.RecipeStepEntity>): List<Instruction> = instructions.map { inst ->
        Instruction(
                stepNo = "Step ${inst.stepNo}",
                stepDesc = inst.stepDescription
        ) }

    fun toIngredientsDomain(ingredients: List<com.mak.feastit.database.entity.IngredientEntity>): List<Ingredient> = ingredients.map { ing ->
        Ingredient(
                id = throw Exception("Delete class No usage"),
                image = ing.ingredientImg,
                localizedName = ing.ingredientName,
                name = ing.ingredientName
        ) }

}