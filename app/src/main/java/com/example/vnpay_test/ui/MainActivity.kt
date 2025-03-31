package com.example.vnpay_test.ui

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.vnpay_test.SharedPreferenceManager
import com.example.vnpay_test.adapter.ImageAdapter
import com.example.vnpay_test.data.ImageRepository
import com.example.vnpay_test.databinding.ActivityMainBinding
import com.example.vnpay_test.viewmodel.ImageViewModel
import com.example.vnpay_test.viewmodel.ImageViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: ImageViewModel

    private val themeTitleList = arrayOf("Light", "Dark", "System")
    private val gridList = arrayOf("1", "2", "3", "4")
    var spanCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestPermissions()

        val sharedPreferenceManager = SharedPreferenceManager(this)
        spanCount = sharedPreferenceManager.grid

        val repository = ImageRepository(contentResolver)
        viewModel = ViewModelProvider(
            this,
            ImageViewModelFactory(repository)
        )[ImageViewModel::class.java]

        val adapter = ImageAdapter { uri, position ->
            openFullImage(uri)
            Log.d("duc", "position: $position ")
        }
        binding.recyclerView.layoutManager = GridLayoutManager(this, spanCount)
        Log.d("duc", "onCreate: $spanCount")
        binding.recyclerView.adapter = adapter

        lifecycleScope.launch {
            viewModel.imageFlow.collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }

        binding.ivTheme.setOnClickListener {
            showDialogChangeTheme()
        }

        binding.ivGrid.setOnClickListener {
            showDialogGrid()
        }
    }

    private fun openFullImage(uri: Uri) {
        val intent = Intent(this, FullImageActivity::class.java).apply {
            putExtra("image_uri", uri.toString())
        }
        startActivity(intent)
    }

    private fun requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(Manifest.permission.READ_MEDIA_IMAGES), 100)
        } else {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 100)
        }
    }

    private fun showDialogChangeTheme() {
        val sharedPreferenceManager = SharedPreferenceManager(this)
        var checkedTheme = sharedPreferenceManager.theme

        val themeDialog = MaterialAlertDialogBuilder(this)
            .setTitle("Theme")
            .setPositiveButton("OK") { _, _ ->
                sharedPreferenceManager.theme = checkedTheme
                AppCompatDelegate.setDefaultNightMode(sharedPreferenceManager.themeFlag[checkedTheme])

            }
            .setNegativeButton("Cancel", null)
            .setSingleChoiceItems(themeTitleList, checkedTheme) { _, which ->
                checkedTheme = which
            }
            .setCancelable(false)
        themeDialog.show()
    }

    private fun showDialogGrid() {
        val sharedPreferenceManager = SharedPreferenceManager(this)
        var checkedGrid = sharedPreferenceManager.grid

        val gridDialog = MaterialAlertDialogBuilder(this)
            .setTitle("Grid")
            .setPositiveButton("OK") { _, _ ->
                sharedPreferenceManager.grid = checkedGrid
                updateGridLayout(checkedGrid)
            }
            .setSingleChoiceItems(gridList, checkedGrid - 1) { _, which ->
                checkedGrid = which + 1
            }
            .setNegativeButton("Cancel", null)
            .setCancelable(false)
        gridDialog.show()
    }

    private fun updateGridLayout(spanCount: Int) {
        val gridLayoutManager = GridLayoutManager(this, spanCount)
        binding.recyclerView.layoutManager = gridLayoutManager
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }
}