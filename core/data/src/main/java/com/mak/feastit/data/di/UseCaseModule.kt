package com.mak.feastit.data.di

import com.mak.feastit.data.repository.ICategoryRepository
import com.mak.feastit.data.repository.IRecipeDetailRepository
import com.mak.feastit.data.repository.IRecipeRepository
import com.mak.feastit.data.usecase.FavRecipeUseCase
import com.mak.feastit.data.usecase.GetCategoriesUseCase
import com.mak.feastit.data.usecase.MealTypeRecipeUseCase
import com.mak.feastit.data.usecase.RecipeDetailUseCase
import com.mak.feastit.data.usecase.SearchRecipeUseCase
import com.mak.feastit.data.usecase.ToggleFavUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
object UseCaseModule {

    @Provides
    fun provideGetCategoriesUseCase(categoryRepository: ICategoryRepository) = GetCategoriesUseCase(categoryRepository)

    @Provides
    fun provideSearchRecipeUseCase(recipeRepository: IRecipeRepository) = SearchRecipeUseCase(recipeRepository)

    @Provides
    fun provideMealTypeRecipeUseCase(recipeRepository: IRecipeRepository) = MealTypeRecipeUseCase(recipeRepository)

    @Provides
    fun provideRecipeDetailUseCase(recipeDetailRepository: IRecipeDetailRepository) = RecipeDetailUseCase(recipeDetailRepository)

    @Provides
    fun provideToggleFavUseCase(recipeDetailRepository: IRecipeDetailRepository) = ToggleFavUseCase(recipeDetailRepository)

    @Provides
    fun provideFavRecipeUseCase(
        recipeRepository: IRecipeRepository
    ) = FavRecipeUseCase(recipeRepository)

}