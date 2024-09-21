package com.mak.feastit.data.di

import com.mak.feastit.domain.repository.ICategoryRepository
import com.mak.feastit.domain.repository.IRecipeDetailRepository
import com.mak.feastit.domain.repository.IRecipeRepository
import com.mak.feastit.domain.usecase.FavRecipeUseCase
import com.mak.feastit.domain.usecase.GetCategoriesUseCase
import com.mak.feastit.domain.usecase.MealTypeRecipeUseCase
import com.mak.feastit.domain.usecase.RecipeDetailUseCase
import com.mak.feastit.domain.usecase.SearchRecipeUseCase
import com.mak.feastit.domain.usecase.ToggleFavUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

//@Module
//@InstallIn(ActivityRetainedComponent::class)
//internal object UseCaseModule {
//
//    @Provides
//    fun provideGetCategoriesUseCase(categoryRepository: ICategoryRepository) = GetCategoriesUseCase(categoryRepository)
//
//    @Provides
//    fun provideSearchRecipeUseCase(recipeRepository: IRecipeRepository) = SearchRecipeUseCase(recipeRepository)
//
//    @Provides
//    fun provideMealTypeRecipeUseCase(recipeRepository: IRecipeRepository) = MealTypeRecipeUseCase(recipeRepository)
//
//    @Provides
//    fun provideRecipeDetailUseCase(recipeDetailRepository: IRecipeDetailRepository) = RecipeDetailUseCase(recipeDetailRepository)
//
//    @Provides
//    fun provideToggleFavUseCase(recipeDetailRepository: IRecipeDetailRepository) = ToggleFavUseCase(recipeDetailRepository)
//
//    @Provides
//    fun provideFavRecipeUseCase(
//        recipeRepository: IRecipeRepository
//    ) = FavRecipeUseCase(recipeRepository)
//
//}