package com.ak.domain.category
import com.ak.domain.base.UseCaseWithParams
import com.ak.domain.utils.RecipeResult

class GetCategoriesUseCase(
    private val repository: CategoryRepository
): UseCaseWithParams<Unit, RecipeResult<List<RecipeCategory>>>() {

    override suspend fun buildUseCase(params: Unit): RecipeResult<List<RecipeCategory>> {
        return repository.getFoodCategories()
    }
}