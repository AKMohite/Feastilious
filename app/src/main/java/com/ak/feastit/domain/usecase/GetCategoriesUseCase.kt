package com.ak.feastit.domain.usecase
import com.ak.feastit.domain.base.UseCaseWithParams
import com.ak.feastit.domain.model.RecipeCategory
import com.ak.feastit.domain.repository.ICategoryRepository
import com.ak.feastit.domain.utils.RecipeResult
import kotlinx.coroutines.flow.Flow

class GetCategoriesUseCase(
    private val repository: ICategoryRepository
): UseCaseWithParams<Unit, Flow<RecipeResult<List<RecipeCategory>>>>() {

    override fun buildUseCase(params: Unit): Flow<RecipeResult<List<RecipeCategory>>> {
        return repository.getFoodCategories()
    }
}