package com.mak.feastit.data.di

import com.mak.feastit.data.repository.CategoryRepository
import com.mak.feastit.data.repository.LegacyRecipeRepository
import com.mak.feastit.data.repository.RealRecipeRepository
import com.mak.feastit.data.repository.RealRecipesRepository
import com.mak.feastit.data.repository.RecipeDetailRepository
import com.mak.feastit.domain.repository.ICategoryRepository
import com.mak.feastit.domain.repository.IRecipeDetailRepository
import com.mak.feastit.domain.repository.IRecipeRepository
import com.mak.feastit.domain.repository.RecipeRepository
import com.mak.feastit.domain.repository.RecipesRepository
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
    abstract fun provideCategoryRepository(repo: CategoryRepository): ICategoryRepository

    @Binds
    abstract fun provideLegacyRecipeRepository(repo: LegacyRecipeRepository): IRecipeRepository

    @Binds
    abstract fun provideRecipeDetailRepository(repo: RecipeDetailRepository): IRecipeDetailRepository
}