package com.ak.feastit.compose.ui.features.collection

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ak.feastit.compose.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CollectionScreen(
    onNavigateToFavorites: () -> Unit,
    onNavigateToShopping: () -> Unit,
    onNavigateToMealPlanner: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(id = R.string.menu_collections)) })
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            item {
                CollectionItem(
                    title = stringResource(id = R.string.favorite_recipes),
                    icon = Icons.Default.Favorite,
                    onClick = onNavigateToFavorites
                )
            }
            item {
                CollectionItem(
                    title = stringResource(id = R.string.shopping_cart),
                    icon = Icons.Default.ShoppingCart,
                    onClick = onNavigateToShopping
                )
            }
            item {
                CollectionItem(
                    title = stringResource(id = R.string.meal_planner),
                    icon = Icons.Default.CalendarMonth,
                    onClick = onNavigateToMealPlanner
                )
            }
        }
    }
}

@Composable
private fun CollectionItem(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    ListItem(
        headlineContent = { Text(title) },
        leadingContent = { Icon(icon, contentDescription = null) },
        modifier = Modifier.clickable { onClick() }
    )
}
