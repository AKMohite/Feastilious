package com.mak.feastit.data.di

import com.mak.feastit.data.repository.LegacyCategoryRepository
import com.mak.feastit.data.repository.LegacyRecipeRepository
import com.mak.feastit.data.repository.RealRecipeRepository
import com.mak.feastit.data.repository.RealRecipesRepository
import com.mak.feastit.data.repository.LegacyRecipeDetailRepository
import com.mak.feastit.data.repository.RealCartRepository
import com.mak.feastit.data.repository.RealStaleRepository
import com.mak.feastit.domain.repository.CartRepository
import com.mak.feastit.domain.repository.ILegacyCategoryRepository
import com.mak.feastit.domain.repository.ILegacyRecipeDetailRepository
import com.mak.feastit.domain.repository.ILegacyRecipeRepository
import com.mak.feastit.domain.repository.RecipeRepository
import com.mak.feastit.domain.repository.RecipesRepository
import com.mak.feastit.domain.repository.StaleRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
internal abstract class RepositoryModule {


    @Binds
    abstract fun bindRecipesRepository(repo: RealRecipesRepository): RecipesRepository

    @Binds
    abstract fun bindRecipeRepository(repo: RealRecipeRepository): RecipeRepository

    @Binds
    abstract fun bindCartRepository(repo: RealCartRepository): CartRepository

    @Binds
    abstract fun bindCategoryRepository(repo: LegacyCategoryRepository): ILegacyCategoryRepository

    @Binds
    abstract fun bindLegacyRecipeRepository(repo: LegacyRecipeRepository): ILegacyRecipeRepository

    @Binds
    abstract fun bindRecipeDetailRepository(repo: LegacyRecipeDetailRepository): ILegacyRecipeDetailRepository
}

@Module
@InstallIn(SingletonComponent::class)
internal abstract class SingletonRepositoryModule {

    @Binds
    abstract fun bindStaleRepository(repo: RealStaleRepository): StaleRepository

}