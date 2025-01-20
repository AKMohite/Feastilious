package com.ak.feastit.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.Adapter.StateRestorationPolicy
import androidx.viewbinding.ViewBinding
import com.ak.feastit.base.BaseFragment
import com.ak.feastit.databinding.FragmentShoppingCartBinding
import com.ak.feastit.ui.cart.component.CartAdapter
import com.ak.feastit.ui.cart.component.CartEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
internal class ShoppingCartFragment : BaseFragment() {

    override fun getViewBinding(inflater: LayoutInflater): ViewBinding =
        FragmentShoppingCartBinding.inflate(inflater)

    private val binding: FragmentShoppingCartBinding
        get() = baseBinding as FragmentShoppingCartBinding

    private val viewModel: ShoppingCartViewModel by viewModels()

    private val adapter: CartAdapter by lazy {
        CartAdapter(
            onCartEvent = ::handleCartEvents
        ).apply {
            stateRestorationPolicy = StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }
    }

    override fun onViewReady(view: View, savedInstanceState: Bundle?) {
        setupView()
        observers()
    }

    private fun setupView() {
        binding.cartItems.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.cartItems.adapter = adapter
        binding.cartGroupBy.setOnCheckedChangeListener { _, btnId ->
            when(btnId) {
                binding.groupByRecipe.id -> viewModel.setCartOrderBy(CartOrderBy.RECIPE)
                binding.groupByAisle.id -> viewModel.setCartOrderBy(CartOrderBy.AISLE)
            }
        }
    }

    private fun observers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest { state ->
                    adapter.reload(state.cart)
                }
            }
        }
    }

    private fun handleCartEvents(cartEvent: CartEvent) {
        when(cartEvent) {
            is CartEvent.ToggleIngredient -> viewModel.toggleIngredient(cartEvent.ingredientId)
            is CartEvent.RemoveRecipe -> viewModel.removeRecipe(cartEvent.recipeId)
            is CartEvent.RecipeDetail -> findNavController().navigate(ShoppingCartFragmentDirections.cartToRecipeDetail(cartEvent.id))
        }
    }


}