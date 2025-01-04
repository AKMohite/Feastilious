package com.mak.feastit.domain.legacyusecase
import com.mak.feastit.domain.model.MealType
import com.mak.feastit.domain.model.RecipeResult
import com.mak.feastit.domain.repository.ILegacyCategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LegacyCategoriesUseCase @Inject constructor(
    private val repository: ILegacyCategoryRepository
): LegacyUseCaseWithParams<Unit, Flow<RecipeResult<List<MealType>>>>() {

    override fun buildUseCase(params: Unit): Flow<RecipeResult<List<MealType>>> {
        return repository.getFoodCategories()
    }
}