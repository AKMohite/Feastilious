package com.ak.feastit.di

import com.ak.feastit.data.local.FeastDatabase
import com.ak.feastit.data.local.base.RecipeDomainMapper
import com.ak.feastit.domain.category.CategoryRepositoryImpl
import com.ak.feastit.data.network.FeastAPIService
import com.ak.feastit.data.network.base.RecipeEntityMapper
import com.ak.feastit.domain.base.DataMapper
import com.ak.feastit.domain.category.CategoryRepository
import com.ak.feastit.domain.recipedetails.RecipeDetailRepository
import com.ak.feastit.domain.recipedetails.RecipeDetailRepositoryImpl
import com.ak.feastit.domain.recipelist.RecipeRepository
import com.ak.feastit.domain.recipelist.RecipeRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
object RepositoryModule {

    @Provides
    fun provideCategoryRepository(): CategoryRepository = CategoryRepositoryImpl()

    @Provides
    fun provideDomainMapper(): RecipeDomainMapper = RecipeDomainMapper()

    @Provides
    fun provideRecipeRepository(
        apiService: FeastAPIService,
        feastDatabase: FeastDatabase,
        recipeDomainMapper: RecipeDomainMapper,
        recipeEntityMapper: RecipeEntityMapper
    ): RecipeRepository = RecipeRepositoryImpl(apiService, feastDatabase, recipeEntityMapper, recipeDomainMapper)

    @Provides
    fun provideRecipeDetailRepository(
            apiService: FeastAPIService
    ): RecipeDetailRepository = RecipeDetailRepositoryImpl(apiService)
}