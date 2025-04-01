package com.example.vnpay_test

import android.graphics.Bitmap
import androidx.collection.LruCache

class ImageCache {
    private val cacheSize = (Runtime.getRuntime().maxMemory() / 1024 / 8).toInt()

    private val memoryCache: LruCache<String, Bitmap> =
        object : LruCache<String, Bitmap>(cacheSize) {
            override fun sizeOf(key: String, bitmap: Bitmap): Int {
                return bitmap.byteCount / 1024
            }
        }

     fun getBitmapFromCache(key: String): Bitmap? {
        return memoryCache.get(key)
    }

    fun addBitmapToCache(key: String, bitmap: Bitmap) {
        if (getBitmapFromCache(key) == null) {
            memoryCache.put(key, bitmap)
        }
    }
}