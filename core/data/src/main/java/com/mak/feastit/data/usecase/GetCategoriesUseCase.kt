package com.mak.feastit.data.usecase
import com.mak.feastit.data.model.MealType
import com.mak.feastit.data.repository.ICategoryRepository
import com.mak.feastit.data.utils.RecipeResult
import kotlinx.coroutines.flow.Flow

class GetCategoriesUseCase(
    private val repository: ICategoryRepository
): com.mak.feastit.data.base.UseCaseWithParams<Unit, Flow<RecipeResult<List<MealType>>>>() {

    override fun buildUseCase(params: Unit): Flow<RecipeResult<List<MealType>>> {
        return repository.getFoodCategories()
    }
}