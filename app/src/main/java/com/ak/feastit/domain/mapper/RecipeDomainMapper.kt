package com.ak.feastit.domain.mapper

import com.ak.feastit.data.local.IngredientEntity
import com.ak.feastit.data.local.InstructionEntity
import com.ak.feastit.data.local.RecipeEntity
import com.ak.feastit.data.local.relations.RecipeDetailEntity
import com.ak.feastit.domain.base.DataMapper
import com.ak.feastit.domain.model.Ingredient
import com.ak.feastit.domain.model.RecipeDetail
import com.ak.feastit.domain.model.Instruction
import com.ak.feastit.domain.model.Recipe

class RecipeDomainMapper: DataMapper<RecipeEntity, Recipe>{

    override fun mapToDomainModel(model: RecipeEntity): Recipe = Recipe(
            id = model.id,
            recipeName = model.recipeName,
            recipeImgUrl = model.recipeImg
    )

    fun toRecipeDetailDomain(detail: RecipeDetailEntity): RecipeDetail {
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

    fun toRecipesDomain(recipes: List<RecipeEntity>): List<Recipe> = recipes.map { recipe-> mapToDomainModel(recipe) }

    fun toInstructionsDomain(instructions: List<InstructionEntity>): List<Instruction> = instructions.map { inst ->
        Instruction(
                stepNo = "Step ${inst.stepNo}",
                stepDesc = inst.stepDesc
        ) }

    fun toIngredientsDomain(ingredients: List<IngredientEntity>): List<Ingredient> = ingredients.map { ing ->
        Ingredient(
                id = ing.ingredientUnique,
                image = ing.ingredientImg,
                localizedName = ing.ingredientName,
                name = ing.ingredientSpec
        ) }

}