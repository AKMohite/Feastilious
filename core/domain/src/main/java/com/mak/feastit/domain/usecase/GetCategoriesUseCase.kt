package com.mak.feastit.domain.usecase
import com.mak.feastit.domain.model.MealType
import com.mak.feastit.domain.model.RecipeResult
import com.mak.feastit.domain.repository.ICategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val repository: ICategoryRepository
): UseCaseWithParams<Unit, Flow<RecipeResult<List<MealType>>>>() {

    override fun buildUseCase(params: Unit): Flow<RecipeResult<List<MealType>>> {
        return repository.getFoodCategories()
    }
}