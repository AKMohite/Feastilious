// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.compose.ui.features.viewall

import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ak.feastit.compose.base.BaseViewModel
import com.ak.feastit.compose.ui.features.explore.ExploreCategory
import com.ak.feastit.compose.utils.getEnumTitle
import com.mak.feastit.domain.model.Recipe
import com.mak.feastit.domain.repository.RecipesRepository
import com.mak.feastit.domain.util.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

@HiltViewModel
internal class ViewAllViewmodel
@Inject
constructor(
  private val repository: RecipesRepository,
  dispatcher: DispatcherProvider,
  private val savedState: SavedStateHandle,
) : BaseViewModel(dispatcher) {
  private val categorySubType: Flow<String?> = savedState.getStateFlow("category_sub_type", null)
  private val _state = MutableStateFlow(ViewAllState())
  val state = _state.asStateFlow()

  fun getPageTitle(): String {
    val category = getCategory().name.getEnumTitle()
    return if (getCategory().isStaticCategory()) {
      val subType = savedState.get<String?>("category_sub_type")
        ?: throw IllegalArgumentException("No sub type found")
      "${category.replace("type", "", ignoreCase = true)}: $subType"
    } else {
      category
    }
  }

  @androidx.paging.ExperimentalPagingApi
  fun observePagedRecipes(): Flow<PagingData<Recipe>> = getExploreCategory()
    .flatMapLatest { category ->
      if (!category.isStaticCategory()) {
        repository.observePaginatedRecipes(
          category.toSyncType(),
          PagingConfig(pageSize = 20, initialLoadSize = 20),
        )
      } else {
        val subType = savedState.get<String?>("category_sub_type")
          ?: throw IllegalArgumentException("No sub type found")
        repository.observeQueryPaginatedRecipes(
          subType,
          PagingConfig(pageSize = 20, initialLoadSize = 20),
        )
      }
    }.cachedIn(uiScope)

  private fun getExploreCategory(): Flow<ExploreCategory> = savedState
    .getStateFlow<String?>("category", null)
    .map { category ->
      ExploreCategory.fromName(category ?: throw IllegalArgumentException("No category found"))
        ?: throw IllegalArgumentException("No explore category found")
    }

  private fun getCategory(): ExploreCategory {
    val category: String =
      savedState["category"] ?: throw IllegalArgumentException("No category found")
    return ExploreCategory.fromName(category)
      ?: throw IllegalArgumentException("No explore category found")
  }
}

internal data class ViewAllState(
  val category: ExploreCategory = ExploreCategory.POPULAR_RECIPES,
  val recipes: PagingData<Recipe> = PagingData.empty(),
)
