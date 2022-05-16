package com.fernando.movieapp.presentation.ui.gallery

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fernando.movieapp.domain.usecases.SaveImagesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class GalleryViewModel @Inject constructor(
    private val saveImagesUseCase: SaveImagesUseCase
) : ViewModel() {

    private val mLiveOSavedImages = MutableLiveData<Boolean>()
    val liveOnSavedImages = mLiveOSavedImages

    private val mIsLoading = MutableLiveData<Boolean>()
    val isLoading = mIsLoading


    fun saveImages(images: List<Uri>?) = viewModelScope.launch(Main) {
        startLoading()
        saveImagesUseCase(images).fold({
            stopLoading()
            mLiveOSavedImages.setValue(true)
        }, ::onError)
    }

    private fun startLoading(){
        mIsLoading.value = true
    }

    private fun stopLoading(){
        mIsLoading.value = false
    }

    private fun onError(exception: Exception) {
        Timber.e(exception)
    }
}