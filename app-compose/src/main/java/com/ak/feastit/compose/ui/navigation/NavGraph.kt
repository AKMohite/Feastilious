package com.ak.feastit.compose.ui.navigation

internal sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Onboarding : Screen("onboarding")
    data object Explore : Screen("explore")
    data object Collection : Screen("collection")
    data object Settings : Screen("settings")
    data object Search : Screen("search")
    data object Favorites : Screen("favorites")
    data object MealPlanner : Screen("meal_planner")
    data object Shopping : Screen("shopping")
    data object RecipeDetail : Screen("recipe_detail/{recipeId}") {
        fun createRoute(recipeId: Long) = "recipe_detail/$recipeId"
    }
}
