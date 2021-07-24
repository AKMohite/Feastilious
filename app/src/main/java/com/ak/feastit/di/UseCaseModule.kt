package com.ak.feastit.di

import com.ak.feastit.data.local.FeastDatabase
import com.ak.feastit.data.local.base.RecipeDomainMapper
import com.ak.feastit.domain.category.CategoryRepository
import com.ak.feastit.domain.category.GetCategoriesUseCase
import com.ak.feastit.domain.favlist.FavRecipeUseCase
import com.ak.feastit.domain.recipedetails.RecipeDetailRepository
import com.ak.feastit.domain.recipedetails.RecipeDetailUseCase
import com.ak.feastit.domain.recipedetails.ToggleFavUseCase
import com.ak.feastit.domain.recipelist.MealTypeRecipeUseCase
import com.ak.feastit.domain.recipelist.RecipeRepository
import com.ak.feastit.domain.recipelist.SearchRecipeUseCase
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