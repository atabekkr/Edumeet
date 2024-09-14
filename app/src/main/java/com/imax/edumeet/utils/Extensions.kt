package com.imax.edumeet.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

fun Fragment.snackBar(message: String) {
    Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
}

fun Long.milliSecondsToTimer(): String {
    val pattern = if (this >= 3_600_000L) "HH:mm:ss" else "mm:ss"
    val simpleDataFormat = SimpleDateFormat(pattern, Locale.ROOT)
    simpleDataFormat.timeZone = TimeZone.getTimeZone("GMT+0")
    return simpleDataFormat.format(this)
}

fun Context.getResourceId(imageName: String): Int {
    val image = imageName.dropLast(4)
    return this.resources.getIdentifier(image, "drawable", this.packageName)
}