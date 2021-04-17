package com.ak.feastit.ui.cart

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ak.feastit.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShoppingListFragment : Fragment(R.layout.shopping_list_fragment) {

    private val viewModel: ShoppingListViewModel by viewModels()


}