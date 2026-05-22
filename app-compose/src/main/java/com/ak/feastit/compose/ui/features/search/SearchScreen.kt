package com.ak.feastit.compose.ui.features.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ak.feastit.compose.R
import com.ak.feastit.ui.search.SearchViewModel
import com.ak.feastit.ui.search.SearchState
import com.ak.feastit.ui.search.SearchAction
import com.mak.feastit.domain.model.Recipe

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SearchScreen(
    onBack: () -> Unit,
    onRecipeClick: (Long) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState(initial = SearchState())
    var query by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.action.collect { action ->
            if (action is SearchAction.OnBackPress) {
                onBack()
            }
        }
    }

    Scaffold(
        topBar = {
            SearchBar(
                query = query,
                onQueryChange = { query = it },
                onSearch = { viewModel.search(it) },
                active = true,
                onActiveChange = { },
                placeholder = { Text(stringResource(id = R.string.search_recipes)) },
                leadingIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(onClick = {
                            query = ""
                            viewModel.clearSearch()
                        }) {
                            Icon(Icons.Default.Clear, contentDescription = null)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                if (state.searchResults.isNotEmpty()) {
                    SearchResultList(recipes = state.searchResults, onRecipeClick = onRecipeClick)
                } else {
                    SuggestionList(
                        recentSearches = state.recentSearches,
                        recommendations = state.recommendations,
                        onSuggestionClick = {
                            query = it
                            viewModel.search(it)
                        },
                        onRecipeClick = onRecipeClick
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding))
    }
}

@Composable
private fun SearchResultList(
    recipes: List<Recipe>,
    onRecipeClick: (Long) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(recipes) { recipe ->
            ListItem(
                headlineContent = { Text(recipe.name) },
                modifier = Modifier.clickable { onRecipeClick(recipe.id) }
            )
        }
    }
}

@Composable
private fun SuggestionList(
    recentSearches: List<String>,
    recommendations: List<Recipe>,
    onSuggestionClick: (String) -> Unit,
    onRecipeClick: (Long) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        if (recentSearches.isNotEmpty()) {
            item {
                Text(
                    text = stringResource(id = R.string.search_recipe_recent_searches),
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(16.dp)
                )
            }
            items(recentSearches) { search ->
                ListItem(
                    headlineContent = { Text(search) },
                    leadingContent = { Icon(Icons.Default.Search, contentDescription = null) },
                    modifier = Modifier.clickable { onSuggestionClick(search) }
                )
            }
        }

        if (recommendations.isNotEmpty()) {
            item {
                Text(
                    text = stringResource(id = R.string.search_recipe_recommendations),
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(16.dp)
                )
            }
            items(recommendations) { recipe ->
                ListItem(
                    headlineContent = { Text(recipe.name) },
                    modifier = Modifier.clickable { onRecipeClick(recipe.id) }
                )
            }
        }
    }
}
