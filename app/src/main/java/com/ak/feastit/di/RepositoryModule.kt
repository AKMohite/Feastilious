package com.ak.feastit.di

import com.ak.feastit.data.local.datasource.RecipeLocalDataSource
import com.ak.feastit.data.network.datasource.RecipeNetworkSource
import com.ak.feastit.domain.repository.ICategoryRepository
import com.ak.feastit.data.repository.CategoryRepository
import com.ak.feastit.domain.repository.IRecipeDetailRepository
import com.ak.feastit.data.repository.RecipeDetailRepository
import com.ak.feastit.domain.repository.IRecipeRepository
import com.ak.feastit.data.repository.RecipeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
object RepositoryModule {

    @Provides
    fun provideCategoryRepository(): ICategoryRepository = CategoryRepository()

    @Provides
    fun provideRecipeRepository(
        networkSource: RecipeNetworkSource,
        localDataSource: RecipeLocalDataSource
    ): IRecipeRepository = RecipeRepository(networkSource, localDataSource)

    @Provides
    fun provideRecipeDetailRepository(
        localDataSource: RecipeLocalDataSource
    ): IRecipeDetailRepository = RecipeDetailRepository(localDataSource)
}