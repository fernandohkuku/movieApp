package com.fernando.movieapp.presentation.ui.gallery

import android.content.ClipData
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import com.fernando.movieapp.R
import com.fernando.movieapp.databinding.FragmentGalleryBinding
import com.fernando.movieapp.presentation.components.contracts.GaleryContract
import com.fernando.ui_ktx.content.allowAction
import com.fernando.ui_ktx.content.allowImages
import com.fernando.ui_ktx.content.goBack
import com.fernando.ui_ktx.content.putAllowMultiple
import com.fernando.ui_ktx.kotlin.isNotNull
import com.wada811.databinding.dataBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class GalleryFragment : Fragment(R.layout.fragment_gallery) {

    private val binding: FragmentGalleryBinding by dataBinding()
    private val viewModel: GalleryViewModel by viewModels()

    private var images = arrayListOf<Uri>()

    private val galleryContent = registerForActivityResult(GaleryContract()) { intent ->
        if (intent.isNotNull() && intent.clipData.isNotNull()) {
            getImagesUri(intent)
        } else {
            getSingleImage(intent)
        }
    }

    private fun getImagesUri(intent: Intent) = with(viewModel) {
        for (i in 0 until intent.clipData!!.itemCount) {
            images.add(intent.clipData!!.getItemAt(i).uri)
            saveImages(images)
        }
    }

    private fun getSingleImage(intent: Intent?) = with(viewModel) {
        if (intent.isNotNull()) {
            images.add(intent.data!!)
            saveImages(images)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        prepareUI()
    }

    private fun isLoading(isLoading: Boolean) = with(binding) {
        progressBar.isGone = !isLoading
    }

    private fun prepareUI() = with(binding) {
        btnUploadImages.setOnClickListener { selectImages() }
    }

    private fun setupObservers() = with(viewModel) {
        isLoading.observe(viewLifecycleOwner, ::isLoading)
        liveOnSavedImages.observe(viewLifecycleOwner) { isSaved ->
            if (isSaved) {
                clearImages()
                Toast.makeText(requireContext(), "The images has been saved", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun selectImages() = Intent().apply {
        galleryContent.launch(this)
    }

    private fun clearImages() {
        images.removeAll(images)
    }

}