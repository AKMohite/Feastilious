package com.mak.feastit.domain.repository

import com.mak.feastit.domain.model.SyncType

interface RecipesRepository {
    suspend fun refreshRecipes(request: SyncType, page: Int)
}