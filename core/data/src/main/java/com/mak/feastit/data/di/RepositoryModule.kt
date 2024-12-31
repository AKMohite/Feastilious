package com.mak.feastit.data.di

import com.mak.feastit.data.repository.LegacyCategoryRepository
import com.mak.feastit.data.repository.LegacyRecipeRepository
import com.mak.feastit.data.repository.RealRecipeRepository
import com.mak.feastit.data.repository.RealRecipesRepository
import com.mak.feastit.data.repository.LegacyRecipeDetailRepository
import com.mak.feastit.data.repository.RealStaleRepository
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

@Module
@InstallIn(ActivityRetainedComponent::class)
internal abstract class RepositoryModule {


    @Binds
    abstract fun provideRecipesRepository(repo: RealRecipesRepository): RecipesRepository

    @Binds
    abstract fun provideRecipeRepository(repo: RealRecipeRepository): RecipeRepository

    @Binds
    abstract fun provideStaleRepository(repo: RealStaleRepository): StaleRepository


    @Binds
    abstract fun provideCategoryRepository(repo: LegacyCategoryRepository): ILegacyCategoryRepository

    @Binds
    abstract fun provideLegacyRecipeRepository(repo: LegacyRecipeRepository): ILegacyRecipeRepository

    @Binds
    abstract fun provideRecipeDetailRepository(repo: LegacyRecipeDetailRepository): ILegacyRecipeDetailRepository
}