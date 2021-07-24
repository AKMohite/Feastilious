package com.ak.feastit.domain.category
import com.ak.feastit.domain.base.UseCaseWithParams
import com.ak.feastit.domain.model.RecipeCategory
import com.ak.feastit.domain.utils.RecipeResult
import kotlinx.coroutines.flow.Flow

class GetCategoriesUseCase(
    private val repository: CategoryRepository
): UseCaseWithParams<Unit, Flow<RecipeResult<List<RecipeCategory>>>>() {

    override fun buildUseCase(params: Unit): Flow<RecipeResult<List<RecipeCategory>>> {
        return repository.getFoodCategories()
    }
}