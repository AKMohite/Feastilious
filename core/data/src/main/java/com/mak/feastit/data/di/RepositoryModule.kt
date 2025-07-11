// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.mak.feastit.data.di

import com.mak.feastit.data.repository.RealCartRepository
import com.mak.feastit.data.repository.RealMealPlanRepository
import com.mak.feastit.data.repository.RealRecipeRepository
import com.mak.feastit.data.repository.RealRecipesRepository
import com.mak.feastit.data.repository.RealStaleRepository
import com.mak.feastit.domain.repository.CartRepository
import com.mak.feastit.domain.repository.MealPlanRepository
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
}

// FIXME: search for worker we need to have single object repositories
@Module
@InstallIn(SingletonComponent::class)
internal abstract class SingletonRepositoryModule {
  @Binds
  abstract fun bindStaleRepository(repo: RealStaleRepository): StaleRepository

  @Binds
  abstract fun bindMealPlanRepository(repo: RealMealPlanRepository): MealPlanRepository
}
