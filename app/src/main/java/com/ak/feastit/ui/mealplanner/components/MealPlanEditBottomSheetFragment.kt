package com.ak.feastit.ui.mealplanner.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.ak.feastit.databinding.FragmentMealPlanEditBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
internal class MealPlanEditBottomSheetFragment: BottomSheetDialogFragment() {

    private var _binding: FragmentMealPlanEditBinding? = null
    private val binding: FragmentMealPlanEditBinding
        get() = _binding!!
    private val viewModel: MealPlannerSheetViewModel by viewModels()
    private val adapter: MealPlanSheetMenuAdapter by lazy {
        MealPlanSheetMenuAdapter(
            onMenuClick = { item ->
                handleMenuClick(item)
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMealPlanEditBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        observers()
    }

    private fun setupView() {
        binding.editMealPlanList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.editMealPlanList.adapter = adapter
    }

    private fun observers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { menuItems ->
                    adapter.reload(menuItems)
                }
            }
        }
    }

    private fun handleMenuClick(item: MealPlanRecipeSheetItem) {
        when (item.action) {
            MealPlanRecipeAction.DOWNLOAD_RECIPE -> {}
            MealPlanRecipeAction.SHARE_RECIPE -> {}
            MealPlanRecipeAction.ADD_TO_SHOPPING_LIST -> {}
            MealPlanRecipeAction.REPEAT_NEXT_WEEK -> {}
            MealPlanRecipeAction.SET_SCHEDULE -> {}
            MealPlanRecipeAction.EDIT_SCHEDULE -> {}
            MealPlanRecipeAction.SET_MEAL_TIME -> {}
            MealPlanRecipeAction.REMOVE_FROM_MEAL_PLAN -> {}
        }
        dismiss()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}