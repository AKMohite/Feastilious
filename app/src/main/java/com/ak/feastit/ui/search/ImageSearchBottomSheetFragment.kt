package com.ak.feastit.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.ak.feastit.databinding.FragmentImageSearchBottomSheetBinding
import com.ak.feastit.utils.onClick
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import timber.log.Timber


internal class ImageSearchBottomSheetFragment: BottomSheetDialogFragment() {

    private var _binding: FragmentImageSearchBottomSheetBinding? = null
    private val binding: FragmentImageSearchBottomSheetBinding
        get() = _binding!!
    private val viewModel: SearchViewModel by viewModels({ requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImageSearchBottomSheetBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cameraLl.onClick {
            Timber.d("On camera click")
            dismiss()
            viewModel.onImageClick(ImageSearch.CAMERA)
        }
        binding.galleryLl.onClick {
            Timber.d("On gallery click")
            dismiss()
            viewModel.onImageClick(ImageSearch.GALLERY)
        }
    }

    companion object {
        fun show(fragmentManager: FragmentManager) {
            ImageSearchBottomSheetFragment().show(fragmentManager, ImageSearchBottomSheetFragment::class.java.name)
        }
    }
}