package com.ak.feastit.ui.viewall

import androidx.lifecycle.SavedStateHandle
import com.ak.feastit.base.BaseViewModel
import com.ak.feastit.ui.explore.ExploreCategory
import com.ak.feastit.utils.getEnumTitle
import com.mak.feastit.domain.util.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class ViewAllViewmodel @Inject constructor(
    private val dispatcher: DispatcherProvider,
    private val savedState: SavedStateHandle
): BaseViewModel(dispatcher) {

    private val exploreCategory: ExploreCategory = getExploreCategory()
    private val categorySubType: String? = savedState["category_sub_type"]
    private val _state = MutableStateFlow(ViewAllState())
    val state = _state.asStateFlow()

    init {
        _state.update { it.copy(category = exploreCategory) }
        getRecipes()
    }

    private fun getRecipes() {
        if (exploreCategory.isStaticCategory()) {
            val subType = categorySubType ?: throw IllegalArgumentException("No sub type found")
            Timber.d("Get recipes for $exploreCategory with sub type $subType")
        } else {
            Timber.d("Get recipes for $exploreCategory")
        }
    }

    private fun getExploreCategory(): ExploreCategory {
        val category: String = savedState["category"] ?: throw IllegalArgumentException("No category found")
        return ExploreCategory.fromName(category) ?: throw IllegalArgumentException("No explore category found")
    }
    
    fun getPageTitle(): String {
        val category = getExploreCategory().name.getEnumTitle()
        return if (exploreCategory.isStaticCategory()) {
            val subType = categorySubType ?: throw IllegalArgumentException("No sub type found")
            "${category.replace("type", "", ignoreCase = true)}: $subType"
        } else {
            category
        }
    }

}

internal data class ViewAllState(
    val category: ExploreCategory = ExploreCategory.POPULAR_RECIPES
)
