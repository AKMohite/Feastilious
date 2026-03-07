// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.ui.search

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewbinding.ViewBinding
import com.ak.feastit.R
import com.ak.feastit.base.BaseFragment
import com.ak.feastit.databinding.FragmentSearchBinding
import com.ak.feastit.ui.search.filter.FilterBottomSheetFragment
import com.ak.feastit.utils.onClick
import com.ak.feastit.utils.show
import com.google.android.material.transition.MaterialElevationScale
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

@AndroidEntryPoint
internal class SearchFragment : BaseFragment() {

  private val viewModel: SearchViewModel by viewModels()

  override fun getViewBinding(inflater: LayoutInflater): ViewBinding = FragmentSearchBinding.inflate(inflater)

  private val binding: FragmentSearchBinding
    get() = baseBinding as FragmentSearchBinding

  private var suggestionAdapter: SearchSuggestionAdapter? = null
  private var resultAdapter: SearchResultAdapter? = null

  private var galleryImageLauncher: ActivityResultLauncher<PickVisualMediaRequest>? = null
  private var captureImageLauncher: ActivityResultLauncher<Uri>? = null
  private var cameraPermissionLauncher: ActivityResultLauncher<String>? = null
  private val onBackPressDispatcher = object : OnBackPressedCallback(true) {
    override fun handleOnBackPressed() {
      handleBackPress()
    }

    fun normalBack() {
      this.isEnabled = false
      requireActivity().onBackPressedDispatcher.onBackPressed()
    }
  }
  private var cameraImagePath: String? = null

  override fun onViewReady(view: View, savedInstanceState: Bundle?) {
    setupView()
    observers()
//        show recent searches and trending recipes in search expanded view and if there is query filter recent searches and search in local db
//        show results in collapsed view with grid same as view all
  }

  private fun observers() {
    viewLifecycleOwner.lifecycleScope.launch {
      repeatOnLifecycle(Lifecycle.State.STARTED) {
        launch {
          viewModel.action.collectLatest { action -> handleAction(action) }
        }
        launch {
          viewModel.state.collectLatest { state ->
            updateRecentAndRecommendations(state)
            updateSearchResults(state)
          }
        }
      }
    }
  }

  private fun updateSearchResults(state: SearchState) {
    binding.searchResults.show(state.searchResults.isNotEmpty())
    resultAdapter?.reload(state.searchResults)
  }

  private fun updateRecentAndRecommendations(state: SearchState) {
    binding.searchSuggestions.show(state.hasRecentSearches())
    val suggestions = state.getSuggestions(
      recentsHeader = getString(R.string.search_recipe_recent_searches),
      recommendationsHeader = getString(R.string.search_recipe_recommendations),
    )
    suggestionAdapter?.submitList(suggestions)
  }

  private fun handleBackPress() {
    Timber.d("On back pressed")
    viewModel.onBackPress()
  }

  private fun setupView() {
    registerContracts()
    binding.searchView.inflateMenu(R.menu.recipe_search)
    binding.searchView.editText.setOnEditorActionListener { view, _, keyEvent ->
      val query = view.text.toString()
      Timber.d("On search editor action: $query")
      Timber.d("On search key: $keyEvent")
      viewModel.search(query)
      return@setOnEditorActionListener true
    }
    binding.searchView.findViewById<ImageView>(com.google.android.material.R.id.open_search_view_clear_button)
      ?.onClick {
        Timber.d("Clear search")
        binding.searchView.editText.setText("")
        viewModel.clearSearch()
      }
    binding.searchView.setOnMenuItemClickListener { menuItem ->
      onMenuItemClick(menuItem)
      return@setOnMenuItemClickListener true
    }
    suggestionAdapter = SearchSuggestionAdapter(
      onRecipeClick = { sharedElements, recipeId ->
        gotoRecipeDetails(sharedElements, recipeId)
      },
      onHistoryClick = { query ->
        Timber.d("On history click: $query")
        binding.searchView.setText(query)
        viewModel.search(query)
      },
    )
    val suggestionLayoutManager = GridLayoutManager(requireContext(), 2)
    suggestionLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
      override fun getSpanSize(position: Int): Int {
        return getSuggestionGridSpanSize(position)
      }
    }
//        binding.searchSuggestions.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    binding.searchSuggestions.layoutManager = suggestionLayoutManager
    binding.searchSuggestions.adapter = suggestionAdapter
    resultAdapter = SearchResultAdapter(
      onRecipeClick = { sharedElements, recipeId ->
        gotoRecipeDetails(sharedElements, recipeId)
      },
    )
    val gridColumnCount =
      requireContext().resources.getInteger(R.integer.search_grid_column_count)
    val resultLayoutManager = GridLayoutManager(requireContext(), gridColumnCount)
    binding.searchResults.layoutManager = resultLayoutManager
    val isTablet = requireContext().resources.getBoolean(R.bool.is_tablet)
    resultLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
      override fun getSpanSize(position: Int): Int {
        return getResultsGridSpanSize(position, isTablet, gridColumnCount)
      }
    }
    binding.searchResults.adapter = resultAdapter
//        binding.searchView.show()
  }

  private fun registerContracts() {
    requireActivity().onBackPressedDispatcher.addCallback(onBackPressDispatcher)
    galleryImageLauncher =
      registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
          Timber.d("Gallery image uri: $uri")
          viewModel.onGalleryImageSelected(uri.toString())
        } else {
          Timber.d("No media selected")
        }
      }
    cameraPermissionLauncher =
      registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
          Timber.d("Camera permission granted")
          launchCamera()
        } else {
          Timber.d("Camera permission denied")
        }
      }
    captureImageLauncher =
      registerForActivityResult(ActivityResultContracts.TakePicture()) { isImageCaptured ->
        Timber.d("Image captured: $isImageCaptured")
        if (isImageCaptured) {
          Timber.d("Image captured and stored at: $cameraImagePath")
          viewModel.imageCaptured()
        }
      }
  }

  private fun unRegisterContracts() {
    onBackPressDispatcher.remove()
    galleryImageLauncher = null
    cameraPermissionLauncher = null
    captureImageLauncher = null
  }

  private fun gotoRecipeDetails(sharedElements: Map<View, String>, recipeId: Long) {
    Timber.d("On recipe click: $recipeId")
    exitTransition = MaterialElevationScale(false).apply {
      duration =
        resources.getInteger(com.google.android.material.R.integer.material_motion_duration_long_1)
          .toLong()
    }
    reenterTransition = MaterialElevationScale(true).apply {
      duration =
        resources.getInteger(com.google.android.material.R.integer.material_motion_duration_long_1)
          .toLong()
    }
    val extras = FragmentNavigatorExtras(
      *sharedElements.toList().toTypedArray(),
    )
    findNavController().navigate(
      directions = SearchFragmentDirections.searchToRecipeDetail(
        recipeId,
      ),
      navigatorExtras = extras,
    )
  }

  /**
   * Trying to create bento box with grids expanding widths depending on [gridColumnCount]
   * @param[position] index of item in list
   * @param[isTablet] is device a tablet
   * @param[gridColumnCount] number of columns in grid
   */
  private fun getResultsGridSpanSize(
    position: Int,
    isTablet: Boolean,
    gridColumnCount: Int,
  ): Int {
    return if (isTablet) {
//            TODO maybe for tablet it can be staggered grid like this: https://stackoverflow.com/a/65511718
      /**
       * here 12 is [gridColumnCount] for tablet
       * With span size for tablet it will be as below:
       * |         7         | |    5     |
       * |   3  | |  3  | |       6       |
       * |    5     | |         7         |
       * |       6       | |   3  | |  3  |
       */
//            TODO maybe some calculations to get the span size
//            here 10 is number after which sequence will be repeated
//            doing this easy way ;P
      val result = position % 10
      when (result) {
        0, 6 -> 7
        1, 5 -> 5
        2, 3, 8, 9 -> 3
        else -> 6 // position = 4, 7
      }
            /*when(result) {
                0 ,1 , 4, 5, 6, 7 -> 2
                else -> 1 // position = 2, 3, 8, 9
            }*/
    } else {
      /**
       * here 5 is [gridColumnCount] for mobile
       * With span size for mobile it will be as below:
       * |      5      |
       * |   3   | | 2 |
       */
//            TODO maybe some calculations to get the span size
//            here 10 is number after which sequence will be repeated
//            doing this easy way ;P
      val result = position % 10
      when (result) {
        0, 5 -> gridColumnCount
        1, 4, 7, 8 -> 3
        else -> 2 // result = 2, 3, 6, 9
      }
    }
  }

  private fun getSuggestionGridSpanSize(position: Int): Int {
    return when (suggestionAdapter?.getType(position)) {
      is SearchSuggestionItem.Recommendation -> 1
      else -> 2
    }
  }

  private fun handleAction(action: SearchAction) {
    Timber.d("on user action: $action")
    when (action) {
      is SearchAction.OnImageClick -> {
        when (action.type) {
          ImageSearch.CAMERA -> {
//                        TODO handle permissions and decline dialog and open camera
            if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA,
              ) == PackageManager.PERMISSION_GRANTED
            ) {
              launchCamera()
            } else {
              cameraPermissionLauncher?.launch(Manifest.permission.CAMERA)
            }
          }

          ImageSearch.GALLERY -> {
//                        check permissions and open gallery
            galleryImageLauncher?.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
          }
        }
      }

      is SearchAction.OnBackPress -> {
        if (binding.searchView.isShowing) {
          binding.searchView.hide()
        } else {
          onBackPressDispatcher.normalBack()
        }
      }
    }
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    Timber.d("On save instance state")
    Timber.d("Camera image path: $cameraImagePath")
    outState.putString("cameraImagePath", cameraImagePath)
  }

  override fun onViewStateRestored(savedInstanceState: Bundle?) {
    super.onViewStateRestored(savedInstanceState)
    Timber.d("On view state restored")
    cameraImagePath = savedInstanceState?.getString("cameraImagePath")
    Timber.d("Camera image path: $cameraImagePath")
  }

  private fun launchCamera() {
    try {
//            TODO instead of doing all this in fragment move this block to viewmodel
      viewLifecycleOwner.lifecycleScope.launch {
        Timber.d("Launch camera to capture image")
        val file = createImageFile()
        cameraImagePath = file.absolutePath
        val photoUri = FileProvider.getUriForFile(
          requireContext(),
          "${requireContext().packageName}.provider",
          file,
        )
        viewModel.setImageUri(photoUri.toString())
        captureImageLauncher?.launch(photoUri)
      }
    } catch (e: Throwable) {
      Timber.e(e, "Error when launching camera")
    }
  }

  // Create a file to store the captured image
//    TODO move file related functions to support class maybe
  private suspend fun createImageFile(): File = withContext(Dispatchers.IO) {
    val imageFileName = "JPEG_" + System.currentTimeMillis() + "_"
    val storagePath = (
      if (VERSION.SDK_INT >= VERSION_CODES.Q) {
        requireContext().filesDir.path
      } else {
        Environment.getExternalStorageDirectory().path
      }
      ) + "/images"
    val storageDir = File(storagePath)
    if (!storageDir.exists()) storageDir.mkdirs()
    val imageFile = File.createTempFile(imageFileName, ".jpg", storageDir)
    imageFile
  }

  private fun onMenuItemClick(menuItem: MenuItem?) {
    if (menuItem == null) return
    when (menuItem.itemId) {
      R.id.search_filter -> {
        Timber.d("On filter clicked")
//                open filters bottom sheet
        FilterBottomSheetFragment.show(childFragmentManager)
      }

      R.id.search_image -> {
        Timber.d("On image clicked")
        ImageSearchBottomSheetFragment.show(childFragmentManager)
      }

      else -> {
        Timber.d("Unknown Menu item clicked")
      }
    }
  }

  override fun onDestroyView() {
    unRegisterContracts()
    suggestionAdapter = null
    resultAdapter = null
    binding.openSearchBar.menu.clear()
    binding.searchView.toolbar.menu.clear()
    super.onDestroyView()
  }
}
