package com.ak.feastit.ui.favorites

import com.mak.feastit.domain.model.Recipe

data class FavoriteState(
    val recipes: List<Recipe> = emptyList()
)