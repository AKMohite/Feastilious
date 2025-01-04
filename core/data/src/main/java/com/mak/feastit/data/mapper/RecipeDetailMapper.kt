package com.mak.feastit.data.mapper

import com.mak.feastit.database.entity.IngredientEntity
import com.mak.feastit.database.entity.RecipeStepEntity
import com.mak.feastit.database.entity.RecipeEntity
import com.mak.feastit.database.entity.ShoppingEntity
import com.mak.feastit.database.entity.SimilarRecipeEntity
import com.mak.feastit.domain.model.IMG_INGREDIENT_BASE_URL
import com.mak.feastit.domain.model.Ingredient
import com.mak.feastit.domain.model.Instruction
import com.mak.feastit.domain.model.Recipe
import com.mak.feastit.domain.model.RecipeDetail
import com.mak.feastit.domain.model.Shopping
import com.mak.feastit.remote.dto.AnalyzedInstructionDTO
import com.mak.feastit.remote.dto.RecipeDTO
import com.mak.feastit.remote.dto.RecipeInformationDTO
import com.mak.feastit.remote.dto.RecipeIngredientDTO
import javax.inject.Inject

internal class RecipeDetailMapper @Inject constructor(): BaseMapper<RecipeInformationDTO, RecipeEntity, RecipeDetail>() {

    override fun jsonToEntity(json: RecipeInformationDTO): RecipeEntity {
        return RecipeEntity(
            id = json.id,
            recipeName = json.title.orEmpty(),
            recipeSummary = json.summary.orEmpty(),
            recipeImg = json.image.orEmpty(),
            recipeReadyInMins = json.readyInMinutes ?: 0,
            servings = json.servings ?: 1,
            pricePerServing = json.pricePerServing ?: 0.0,
            sourceName = json.sourceName.orEmpty(),
            recipeSource = json.sourceUrl.orEmpty(),
            isAddedToCollection = false,
            cuisines = get(json.cuisines),
            dishTypes = get(json.dishTypes),
            diets = get(json.diets)
        )
    }

    override fun entityToModel(entity: RecipeEntity): RecipeDetail {
        return RecipeDetail(
            recipeId = entity.id,
            recipeName = entity.recipeName,
            recipeSummary = entity.recipeSummary,
            recipeImg = entity.recipeImg,
            recipeSource = entity.recipeSource,
            recipeReadyInMins = entity.recipeReadyInMins,
            servings = entity.servings,
            pricePerServing = entity.pricePerServing,
            sourceName = entity.sourceName,
            isAddedToCollection = entity.isAddedToCollection,
//            ingredients = toIngredientsDomain(detail.ingredients),
//            instructions = toInstructionsDomain(detail.instructions)
        )
    }

    private fun get(cuisines: List<String>?): String {
        return cuisines?.joinToString(separator = ",") ?: ""
    }

    fun jsonToIngredientsEntity(recipeId: Long, ingredients: List<RecipeIngredientDTO>?): List<IngredientEntity> {
        return ingredients?.map { dto ->
            IngredientEntity(
                id = "${recipeId}_${dto.id}",
                ingredientId = dto.id,
                aisle = dto.aisle ?: "No category",
                recipeId = recipeId,
                ingredientName = dto.name.orEmpty(),
                amount = dto.amount ?: 0.0,
                unit = dto.unit.orEmpty(),
                ingredientImg = if (!dto.image.isNullOrBlank()) "$IMG_INGREDIENT_BASE_URL${dto.image}" else "",
            )
        } ?: return emptyList()
    }

    fun jsonToAnalysedIngredientsEntity(
        id: Long,
        instructionsDTO: List<AnalyzedInstructionDTO>
    ): List<IngredientEntity> {
        val ingredientDTOs = instructionsDTO.flatMap { instruction ->
            instruction.steps ?: emptyList()
        }.flatMap { step ->
            step.ingredients ?: emptyList()
        }.distinctBy { ingredient -> ingredient.id }
        return ingredientDTOs.map { dto ->
            IngredientEntity(
                id = "${id}_${dto.id}",
                ingredientId = dto.id,
                aisle = "No category",
                recipeId = id,
                ingredientName = dto.name.orEmpty(),
                amount = 0.0,
                unit = "",
                ingredientImg = dto.image.orEmpty()
            )
        }
    }

    fun jsonToStepEntities(id: Long, instructionsDTO: List<AnalyzedInstructionDTO>): List<RecipeStepEntity> {
        val dtos = instructionsDTO.mapIndexed { index: Int, analyzedInstructionDTO: AnalyzedInstructionDTO ->
            val stepName = analyzedInstructionDTO.name ?: "Analysed Steps $index"
            Pair(stepName, analyzedInstructionDTO.steps)
        }.mapNotNull { (key, value) ->
            if (value.isNullOrEmpty()) return@mapNotNull null
            Pair(key, value)
        }
        val entities = dtos.flatMap { (name, steps) ->
            steps.map { step ->
                RecipeStepEntity(
                    stepId = "${id}_${step.number}",
                    recipeId = id,
                    stepNo = step.number ?: 0,
                    stepDescription = step.step.orEmpty(),
                    stepName = name
                )
            }
        }
        return entities
    }

    fun jsonToRecipeEntities(dtos: List<RecipeDTO>, existingRecipes: Map<Long, RecipeEntity>): List<RecipeEntity> {
        return dtos.map { dto ->
            val existingRecipe = existingRecipes[dto.id]
            val cuisines = existingRecipe?.cuisines ?: get(dto.cuisines)
            val dishTypes = existingRecipe?.dishTypes ?: get(dto.dishTypes)
            val diets = existingRecipe?.diets ?: get(dto.diets)
            RecipeEntity(
                id = dto.id,
                recipeName = dto.title,
                recipeSummary = dto.summary ?: existingRecipe?.recipeSummary.orEmpty(),
                recipeImg = dto.image?.ifEmpty { existingRecipe?.recipeImg.orEmpty() } ?: existingRecipe?.recipeImg.orEmpty(),
                recipeReadyInMins = dto.readyInMinutes ?: existingRecipe?.recipeReadyInMins ?: 0,
                servings = dto.servings ?: existingRecipe?.servings ?:  0,
                pricePerServing = dto.pricePerServing ?: existingRecipe?.pricePerServing ?: 0.0,
                sourceName = dto.sourceName ?: existingRecipe?.sourceName.orEmpty(),
                recipeSource = dto.sourceUrl ?: existingRecipe?.recipeSource.orEmpty(),
                isAddedToCollection = false,
                cuisines = cuisines,
                dishTypes = dishTypes,
                diets = diets
            )
        }
    }

    fun jsonToSimilarEntities(dtos: List<RecipeDTO>, id: Long): List<SimilarRecipeEntity> {
        return dtos.map { dto ->
            SimilarRecipeEntity(
                id = "${id}_${dto.id}",
                recipeId = dto.id,
                parentRecipeId = id
            )
        }
    }

    fun entityToRecipe(entities: List<RecipeEntity>): List<Recipe> {
        return entities.map { entity ->
            Recipe(
                id = entity.id,
                recipeName = entity.recipeName,
                recipeImgUrl = entity.recipeImg
            )
        }
    }

    fun entitiesToIngredients(entities: List<IngredientEntity>): List<Ingredient> {
        return entities.map { entity ->
            Ingredient(
                id = entity.id,
                image = entity.ingredientImg,
                localizedName = entity.ingredientName,
                name = entity.ingredientName
            )
        }
    }

    fun entitiesToSteps(entities: List<RecipeStepEntity>): List<Instruction> {
        return entities.map { entity ->
            Instruction(
                stepNo = "Step ${entity.stepNo}",
                stepDesc = entity.stepDescription
            )
        }
    }

    fun ingredientsToShoppingCarts(ingredients: List<IngredientEntity>): List<ShoppingEntity> {
        return ingredients.map { entity ->
            ingredientToShoppingCart(entity)
        }
    }

    fun ingredientToShoppingCart(entity: IngredientEntity): ShoppingEntity {
        return ShoppingEntity(
            id = entity.id,
            recipeId = entity.recipeId,
            isBought = false
        )
    }

    fun entityToShopping(entities: List<ShoppingEntity>): List<Shopping> {
        return entities.map { entity ->
            Shopping(
                id = entity.id,
                isBought = entity.isBought
            )
        }
    }
}