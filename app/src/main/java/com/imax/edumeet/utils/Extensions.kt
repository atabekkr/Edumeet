package com.imax.edumeet.utils

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
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

fun uploadImage(uri: Uri?, contentResolver: ContentResolver): MultipartBody.Part? {
    if (uri == null) return null
    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
    val byteArrayOutputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream)
    val byteArray = byteArrayOutputStream.toByteArray()
    if (byteArray.size / 1024 > 400) {
        val byteArrayOutputStreamAfterResize = ByteArrayOutputStream()
        val bitmapAfterResize = resizePhoto(bitmap)
        bitmapAfterResize.compress(
            Bitmap.CompressFormat.JPEG,
            100,
            byteArrayOutputStreamAfterResize
        )
        val byteArrayAfterResize = byteArrayOutputStreamAfterResize.toByteArray()
        val imgAfterResize = RequestBody.create("image/*".toMediaTypeOrNull(), byteArrayAfterResize)
        return MultipartBody.Part.createFormData("file", File(uri.path!!).name, imgAfterResize)
    }
    val img = RequestBody.create("image/*".toMediaTypeOrNull(), byteArray)
    return MultipartBody.Part.createFormData("file", File(uri.path!!).name, img)
}

fun resizePhoto(bitmap: Bitmap): Bitmap {

    val w = bitmap.width
    val h = bitmap.height
    val aspRat = w / h
    val W = 400
    var H = W * aspRat
    if (H == 0) H = 400
    Log.d("SizePhoto", "$W, $H")
    val b = Bitmap.createScaledBitmap(bitmap, W, H, false)

    return b


}