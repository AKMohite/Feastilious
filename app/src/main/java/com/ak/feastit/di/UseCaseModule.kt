package com.ak.feastit.di

import com.ak.feastit.data.local.FeastDatabase
import com.ak.feastit.data.local.base.RecipeDomainMapper
import com.ak.feastit.domain.repository.CategoryRepository
import com.ak.feastit.domain.usecase.GetCategoriesUseCase
import com.ak.feastit.domain.usecase.FavRecipeUseCase
import com.ak.feastit.domain.repository.RecipeDetailRepository
import com.ak.feastit.domain.usecase.RecipeDetailUseCase
import com.ak.feastit.domain.recipedetails.ToggleFavUseCase
import com.ak.feastit.domain.usecase.MealTypeRecipeUseCase
import com.ak.feastit.domain.repository.RecipeRepository
import com.ak.feastit.domain.usecase.SearchRecipeUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
object UseCaseModule {

    @Provides
    fun provideGetCategoriesUseCase(categoryRepository: CategoryRepository) = GetCategoriesUseCase(categoryRepository)

    @Provides
    fun provideSearchRecipeUseCase(recipeRepository: RecipeRepository) = SearchRecipeUseCase(recipeRepository)

    @Provides
    fun provideMealTypeRecipeUseCase(recipeRepository: RecipeRepository) = MealTypeRecipeUseCase(recipeRepository)

    @Provides
    fun provideRecipeDetailUseCase(recipeDetailRepository: RecipeDetailRepository) = RecipeDetailUseCase(recipeDetailRepository)

    @Provides
    fun provideToggleFavUseCase(recipeDetailRepository: RecipeDetailRepository) = ToggleFavUseCase(recipeDetailRepository)

    @Provides
    fun provideFavRecipeUseCase(
            feastDatabase: FeastDatabase,
            recipeDomainMapper: RecipeDomainMapper
    ) = FavRecipeUseCase(feastDatabase, recipeDomainMapper)

}