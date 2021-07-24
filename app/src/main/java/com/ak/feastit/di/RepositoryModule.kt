package com.ak.feastit.di

import com.ak.feastit.data.local.datasource.RecipeLocalDataSource
import com.ak.feastit.data.network.datasource.RecipeNetworkSource
import com.ak.feastit.domain.repository.ICategoryRepository
import com.ak.feastit.data.repository.CategoryRepositoryImpl
import com.ak.feastit.domain.repository.IRecipeDetailRepository
import com.ak.feastit.data.repository.RecipeDetailRepositoryImpl
import com.ak.feastit.domain.repository.IRecipeRepository
import com.ak.feastit.data.repository.RecipeRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
object RepositoryModule {

    @Provides
    fun provideCategoryRepository(): ICategoryRepository = CategoryRepositoryImpl()

    @Provides
    fun provideRecipeRepository(
        networkSource: RecipeNetworkSource,
        localDataSource: RecipeLocalDataSource
    ): IRecipeRepository = RecipeRepositoryImpl(networkSource, localDataSource)

    @Provides
    fun provideRecipeDetailRepository(
        localDataSource: RecipeLocalDataSource
    ): IRecipeDetailRepository = RecipeDetailRepositoryImpl(localDataSource)
}