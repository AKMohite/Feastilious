package com.ak.domain.category
import com.ak.domain.base.UseCaseWithParams
import com.ak.domain.utils.RecipeResult

class GetCategoriesUseCase(
    private val repository: CategoryRepository
): UseCaseWithParams<Unit, RecipeResult<Exception, List<RecipeCategory>>>() {

    override fun buildUseCase(params: Unit): RecipeResult<Exception, List<RecipeCategory>> {
        return repository.getFoodCategories()
    }
}