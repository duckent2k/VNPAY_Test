package com.example.vnpay_test.data

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.paging.PagingSource
import androidx.paging.PagingState

class ImagePagingSource(private val repository: ImageRepository) : PagingSource<Int, Uri>() {
    override fun getRefreshKey(state: PagingState<Int, Uri>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Uri> {
        val page = params.key ?: 0
        val images = repository.getImages(params.loadSize, page * params.loadSize)

        return LoadResult.Page(
            data = images,
            prevKey = if (page == 0) null else page - 1,
            nextKey = if (images.isEmpty()) null else page + 1
        )
    }
}