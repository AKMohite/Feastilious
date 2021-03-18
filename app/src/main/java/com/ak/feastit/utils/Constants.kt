package com.ak.feastit.utils

const val APP_TAG = "FeastIt"
const val DEFAULT_PAGE_SIZE = "30"

// API Query Keys
const val QUERY_SEARCH = "query"
const val QUERY_NUMBER = "number"
const val QUERY_OFFSET = "offset"
const val QUERY_API_KEY = "apiKey"
const val QUERY_TYPE = "type"
const val QUERY_DIET = "diet"
const val QUERY_ADD_RECIPE_INFORMATION = "addRecipeInformation"
const val QUERY_FILL_INGREDIENTS = "fillIngredients"

const val API_PATH_ID = "recipeId"
const val API_GET_RANDOM_RECIPES = "/recipes/random"
const val API_COMPLEX_SEARCH_RECIPES = "/recipes/complexSearch"
const val API_GET_RECIPE_ANALYSED_INSTRUCTION = "/recipes/{${API_PATH_ID}}/analyzedInstructions"