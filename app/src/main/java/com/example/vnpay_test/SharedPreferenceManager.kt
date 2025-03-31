package com.example.vnpay_test

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

class SharedPreferenceManager(context: Context) {
    private val sharedPreferences =
        context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)

    private val editor = sharedPreferences.edit()

    private val keyTheme = "theme"
    private val gridKey = "grid"

    var theme
        get() = sharedPreferences.getInt(keyTheme, 2)
        set(value) {
            editor.putInt(keyTheme, value)
            editor.apply()
        }

    val themeFlag = arrayOf(
        AppCompatDelegate.MODE_NIGHT_NO,
        AppCompatDelegate.MODE_NIGHT_YES,
        AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    )

    var grid
        get() = sharedPreferences.getInt(gridKey, 1)
        set(value) {
            editor.putInt(gridKey, value)
            editor.apply()
        }
}