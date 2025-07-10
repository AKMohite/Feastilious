package com.ak.feastit.ui.viewall

import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ak.feastit.base.BaseViewModel
import com.ak.feastit.ui.explore.ExploreCategory
import com.ak.feastit.utils.getEnumTitle
import com.mak.feastit.domain.model.Recipe
import com.mak.feastit.domain.repository.RecipesRepository
import com.mak.feastit.domain.util.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class ViewAllViewmodel @Inject constructor(
    private val repository: RecipesRepository,
    dispatcher: DispatcherProvider,
    private val savedState: SavedStateHandle
): BaseViewModel(dispatcher) {

    private val categorySubType: Flow<String?> = savedState.getStateFlow("category_sub_type", null)
    private val _state = MutableStateFlow(ViewAllState())
    val state = _state.asStateFlow()

    init {
//        _state.update { it.copy(category = exploreCategory) }
//        getRecipes()
    }

    fun getPageTitle(): String {
        val category = getCategory().name.getEnumTitle()
        return if (getCategory().isStaticCategory()) {
            val subType = savedState.get<String?>("category_sub_type") ?: throw IllegalArgumentException("No sub type found")
            "${category.replace("type", "", ignoreCase = true)}: $subType"
        } else {
            category
        }
    }

    fun observePagedRecipes(): Flow<PagingData<Recipe>> {
        return getExploreCategory()
            .flatMapLatest { category ->
                if(!category.isStaticCategory()) {
                    repository.observePaginatedRecipes(category.toSyncType(), PagingConfig(pageSize = 20, initialLoadSize = 20))
                } else {
                    val subType = savedState.get<String?>("category_sub_type") ?: throw IllegalArgumentException("No sub type found")
                    repository.observeQueryPaginatedRecipes(subType, PagingConfig(pageSize = 20, initialLoadSize = 20))
                }
            }.cachedIn(uiScope)
    }

//    private fun getRecipes() {
//        if (exploreCategory.isStaticCategory()) {
//            val subType = categorySubType ?: throw IllegalArgumentException("No sub type found")
//            Timber.d("Get recipes for $exploreCategory with sub type $subType")
//
//        } else {
//            Timber.d("Get recipes for $exploreCategory")
//            repository.observePaginatedRecipes(exploreCategory.toSyncType(), PagingConfig(pageSize = 20, initialLoadSize = 60))
//                .onEach { pagingData ->
//                    _state.update { it.copy(recipes = pagingData) }
//                }.launchIn(uiScope)
//        }
//    }

    private fun getExploreCategory(): Flow<ExploreCategory> {
        return savedState.getStateFlow<String?>("category", null)
            .map { category ->
                ExploreCategory.fromName(category ?: throw IllegalArgumentException("No category found")) ?: throw IllegalArgumentException("No explore category found")
            }
    }

    private fun getCategory(): ExploreCategory {
        val category: String =
            savedState["category"] ?: throw IllegalArgumentException("No category found")
        return ExploreCategory.fromName(category)
            ?: throw IllegalArgumentException("No explore category found")
    }

}

internal data class ViewAllState(
    val category: ExploreCategory = ExploreCategory.POPULAR_RECIPES, // default empty state
    val recipes: PagingData<Recipe> = PagingData.empty()
)
