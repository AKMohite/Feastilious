package com.ak.feastit.ui.search

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import com.ak.feastit.R
import com.ak.feastit.base.BaseFragment
import com.ak.feastit.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
internal class SearchFragment : BaseFragment() {

    private val viewModel: SearchViewModel by viewModels()

    override fun getViewBinding(inflater: LayoutInflater): ViewBinding? = FragmentSearchBinding.inflate(inflater)

    private val binding: FragmentSearchBinding
        get() = baseBinding as FragmentSearchBinding

    private var pickGalleryImage: ActivityResultLauncher<PickVisualMediaRequest>? = null
    private var cameraPermissionLauncher: ActivityResultLauncher<String>? = null

    override fun onViewReady(view: View, savedInstanceState: Bundle?) {
        pickGalleryImage = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                Timber.d("Gallery image uri: $uri")
            } else {
                Timber.d("No media selected")
            }
        }
        cameraPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                Timber.d("Camera permission granted")
                launchCamera()
            } else {
                Timber.d("Camera permission denied")
            }
        }
        binding.openSearchBar.inflateMenu(R.menu.recipe_search)
        binding.searchView.inflateMenu(R.menu.recipe_search)
        // this is to launch search view
        binding.openSearchBar.performClick()
        binding.openSearchBar.setOnMenuItemClickListener { menuItem ->
            onMenuItemClick(menuItem)
            return@setOnMenuItemClickListener true
        }
        binding.searchView.setOnMenuItemClickListener { menuItem ->
            onMenuItemClick(menuItem)
            return@setOnMenuItemClickListener true
        }
        viewModel.reload()
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.action.collectLatest { action -> handleAction(action) }
            }
        }
//        show recent searches and trending recipes in search expanded view and if there is query filter recent searches and search in local db
//        show results in collapsed view with grid same as view all
    }

    private fun handleAction(action: SearchAction) {
        Timber.d("on user action: $action")
        when (action) {
            is SearchAction.OnImageClick -> {
                when(action.type) {
                    ImageSearch.CAMERA -> {
//                        TODO handle permissions and decline dialog and open camera
                        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                            launchCamera()
                        } else {
                            cameraPermissionLauncher?.launch(Manifest.permission.CAMERA)
                        }
                    }
                    ImageSearch.GALLERY -> {
//                        check permissions and open gallery
                        pickGalleryImage?.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    }
                }
            }
        }
    }

    private fun launchCamera() {
        Timber.d("Launch camera to capture image")
    }

    private fun onMenuItemClick(menuItem: MenuItem?) {
        if (menuItem == null) return
        when(menuItem.itemId) {
            R.id.search_filter -> {
                Timber.d("On filter clicked")
//                open filters bottom sheet
            }
            R.id.search_image -> {
                Timber.d("On image clicked")
                ImageSearchBottomSheetFragment.show(childFragmentManager)
            }
        }
    }

    override fun onDestroyView() {
        binding.openSearchBar.menu.clear()
        binding.searchView.toolbar.menu.clear()
        super.onDestroyView()
    }

}