package com.ak.feastit.compose.ui.features.explore

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.ak.feastit.compose.R
import com.ak.feastit.ui.explore.*
import com.ak.feastit.utils.getEnumTitle
import com.mak.feastit.domain.model.Recipe

@Composable
internal fun ExploreScreen(
    onRecipeClick: (Long) -> Unit,
    viewModel: ExploreViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    if (state.isLoading) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(androidx.compose.ui.Alignment.Center))
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            items(state.displayableSections()) { section ->
                ExploreSection(section, onRecipeClick)
            }
        }
    }
}

@Composable
private fun ExploreSection(
    section: ExploreAdapterItem,
    onRecipeClick: (Long) -> Unit
) {
    Column {
        Text(
            text = section.category.name.getEnumTitle(),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        when (section) {
            is ExploreAdapterItem.HorizontalChips -> {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(section.items) { chip ->
                        FilterChip(
                            selected = false,
                            onClick = { /* TODO */ },
                            label = { Text(chip.title) }
                        )
                    }
                }
            }
            is ExploreAdapterItem.HorizontalRecipes -> {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(section.items) { recipe ->
                        RecipeCard(recipe = recipe as Recipe, onClick = { onRecipeClick(recipe.id) })
                    }
                }
            }
            is ExploreAdapterItem.TopBanner -> {
                // TODO: Implement Banner
            }
        }
    }
}

@Composable
private fun RecipeCard(
    recipe: Recipe,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.width(200.dp)
    ) {
        Column {
            AsyncImage(
                model = recipe.image.getResource(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentScale = ContentScale.Crop
            )
            Text(
                text = recipe.name,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(8.dp),
                maxLines = 2
            )
        }
    }
}
