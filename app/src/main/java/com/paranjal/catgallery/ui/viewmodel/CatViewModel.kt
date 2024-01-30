package com.paranjal.catgallery.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paranjal.catgallery.data.CatImage
import com.paranjal.catgallery.service.CatApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CatViewModel @Inject constructor(
    private val catApiService: CatApiService
) : ViewModel() {

    private val _catImages = MutableLiveData<List<CatImage>>()
    val catImages: LiveData<List<CatImage>> get() = _catImages

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> get() = _isError

    fun fetchCatImages() {
        _isLoading.value = true

        viewModelScope.launch {
            try {
                _catImages.value = catApiService.getCatImages()
            } catch (e: Exception) {
                _isError.value = true
            } finally {
                _isLoading.value = false
            }
        }
    }
}
