package com.ak.feastit.ui.collection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.ak.feastit.base.BaseFragment
import com.ak.feastit.databinding.FragmentCollectionsBinding
import com.ak.feastit.utils.onClick

internal class CollectionFragment: BaseFragment() {

    override fun getViewBinding(inflater: LayoutInflater): ViewBinding =
        FragmentCollectionsBinding.inflate(inflater)

    private val binding: FragmentCollectionsBinding
        get() = baseBinding as FragmentCollectionsBinding

    override fun onViewReady(view: View, savedInstanceState: Bundle?) {
        binding.collectionFavorite.onClick {
            findNavController().navigate(CollectionFragmentDirections.collectionToFavorites())
        }

        binding.collectionShoppingCart.onClick {
            findNavController().navigate(CollectionFragmentDirections.collectionToShopping())
        }

        binding.collectionMealPlanner.onClick {
            findNavController().navigate(CollectionFragmentDirections.collectionToMealPlanner())
        }

        binding.collectionMealScheduled.onClick {  }
    }
}