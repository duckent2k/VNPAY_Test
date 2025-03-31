package com.example.vnpay_test.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.vnpay_test.data.ImagePagingSource
import com.example.vnpay_test.data.ImageRepository

class ImageViewModel(private val repository: ImageRepository) : ViewModel() {
    val imageFlow = Pager(
        PagingConfig(
            pageSize = 10,
            prefetchDistance = 5,
            enablePlaceholders = false
        )
    ) {
        ImagePagingSource(repository)
    }.flow.cachedIn(viewModelScope)
}