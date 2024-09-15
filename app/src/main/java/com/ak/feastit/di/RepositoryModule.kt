package com.ak.feastit.di

import com.ak.feastit.domain.repository.CategoryRepository
import com.ak.feastit.domain.repository.ICategoryRepository
import com.ak.feastit.domain.repository.IRecipeDetailRepository
import com.ak.feastit.domain.repository.IRecipeRepository
import com.ak.feastit.domain.repository.RecipeDetailRepository
import com.ak.feastit.domain.repository.RecipeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun provideCategoryRepository(repo: CategoryRepository): ICategoryRepository

    @Binds
    abstract fun provideRecipeRepository(repo: RecipeRepository): IRecipeRepository

    @Binds
    abstract fun provideRecipeDetailRepository(repo: RecipeDetailRepository): IRecipeDetailRepository
}