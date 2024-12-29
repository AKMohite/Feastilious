package com.mak.feastit.data.repository

import com.mak.feastit.data.mapper.LegacyRecipeDomainMapper
import com.mak.feastit.domain.model.RecipeDetail
import com.mak.feastit.domain.model.RecipeResult
import com.mak.feastit.database.FeastDB
import com.mak.feastit.domain.repository.ILegacyRecipeDetailRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class LegacyRecipeDetailRepository @Inject constructor(
    private val db: FeastDB
) : ILegacyRecipeDetailRepository {

    private val domainMapper = LegacyRecipeDomainMapper()

    override fun getRecipeDetail(id: Long): Flow<RecipeResult<RecipeDetail>> = flow {
        try {
            emit(RecipeResult.loading())
            val recipe = domainMapper.toRecipeDetailDomain(db.recipeDAO().getRecipeDetail(recipeId = id))
            emit(RecipeResult.success(recipe))
        } catch (error: Exception) {
            emit(RecipeResult.error(error.message ?: "An error occurred"))
        }
    }

    override fun toggleRecipeFav(id: Long, isAdded: Boolean): Flow<RecipeResult<Boolean>> {
        return flow {
            try {
                emit(RecipeResult.loading())
                val isUpdated = (db.recipeDAO().toggleFav(id, isAdded) > 0)
                emit(RecipeResult.success(isUpdated))
            } catch (error: Exception) {
                emit(RecipeResult.error(error.message ?: "An error occurred"))
            }
        }
    }
}