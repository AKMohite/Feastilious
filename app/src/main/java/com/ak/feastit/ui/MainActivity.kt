package com.ak.feastit.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.ak.domain.category.RecipeCategory
import com.ak.domain.utils.RecipeResult
import com.ak.feastit.R
import com.ak.feastit.ui.recipes.RecipeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    val recipeViewModel: RecipeViewModel by viewModels()
    lateinit var text: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        text = findViewById(R.id.text)
        recipeViewModel.allCategories.observe(this, Observer { state ->
            renderUI(state)
        })

    }

    private fun renderUI(state: RecipeResult<List<RecipeCategory>>) {
        when (state) {
            is RecipeResult.Success -> {
                var categories = ""
                for (single in state.result) {
                    categories += "${single.categoryName}\n"
                }

                text.text = categories

            }
            is RecipeResult.Error -> {}
            RecipeResult.Loading -> {}
        }
    }


}