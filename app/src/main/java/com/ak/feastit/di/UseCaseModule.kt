package com.ak.feastit.di

import com.ak.feastit.domain.repository.ICategoryRepository
import com.ak.feastit.domain.usecase.GetCategoriesUseCase
import com.ak.feastit.domain.usecase.FavRecipeUseCase
import com.ak.feastit.domain.repository.IRecipeDetailRepository
import com.ak.feastit.domain.usecase.RecipeDetailUseCase
import com.ak.feastit.domain.usecase.ToggleFavUseCase
import com.ak.feastit.domain.usecase.MealTypeRecipeUseCase
import com.ak.feastit.domain.repository.IRecipeRepository
import com.ak.feastit.domain.usecase.SearchRecipeUseCase
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