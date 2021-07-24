package com.ak.feastit.di

import com.ak.feastit.domain.datasource.IRecipeLocalDataSource
import com.ak.feastit.domain.datasource.IRecipeNetworkSource
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
        networkSource: IRecipeNetworkSource,
        localDataSource: IRecipeLocalDataSource
    ): IRecipeRepository = RecipeRepository(networkSource, localDataSource)

    @Provides
    fun provideRecipeDetailRepository(
        localDataSource: IRecipeLocalDataSource
    ): IRecipeDetailRepository = RecipeDetailRepository(localDataSource)
}