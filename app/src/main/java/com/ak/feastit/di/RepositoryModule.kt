package com.ak.feastit.di

import com.ak.feastit.data.local.datasource.RecipeLocalDataSource
import com.ak.feastit.data.network.datasource.RecipeNetworkSource
import com.ak.feastit.domain.repository.CategoryRepository
import com.ak.feastit.data.repository.CategoryRepositoryImpl
import com.ak.feastit.domain.repository.RecipeDetailRepository
import com.ak.feastit.data.repository.RecipeDetailRepositoryImpl
import com.ak.feastit.domain.repository.RecipeRepository
import com.ak.feastit.data.repository.RecipeRepositoryImpl
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
    fun provideRecipeRepository(
        networkSource: RecipeNetworkSource,
        localDataSource: RecipeLocalDataSource
    ): RecipeRepository = RecipeRepositoryImpl(networkSource, localDataSource)

    @Provides
    fun provideRecipeDetailRepository(
        localDataSource: RecipeLocalDataSource
    ): RecipeDetailRepository = RecipeDetailRepositoryImpl(localDataSource)
}