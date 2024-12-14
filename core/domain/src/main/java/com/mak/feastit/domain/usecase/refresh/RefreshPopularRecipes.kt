package com.mak.feastit.domain.usecase.refresh

import com.mak.feastit.domain.common.PaginatedParams
import com.mak.feastit.domain.common.RefreshableContent
import com.mak.feastit.domain.model.Recipe
import com.mak.feastit.domain.model.SyncType
import com.mak.feastit.domain.repository.LastSyncRepository
import com.mak.feastit.domain.repository.RecipesRepository

interface FetchPopularRecipes: RefreshableContent<PaginatedParams, List<Recipe>>

internal class RefreshPopularRecipes(
    private val recipesRepository: RecipesRepository,
    private val lastSyncRepository: LastSyncRepository
): FetchPopularRecipes {

    override suspend fun invoke(params: PaginatedParams): List<Recipe> {
        return if (lastSyncRepository.needSync(SyncType.POPULAR_RECIPES)) {
            val recipes = recipesRepository.fetchRecipeFor(params.page)
            if (params.page == 1) {
//                recipesRepository.saveRecipes(recipes)
                lastSyncRepository.updateLastSync(SyncType.POPULAR_RECIPES)
            }
            recipes
        } else {
    //            TODO return list from repository?
            emptyList()
        }
    }
}