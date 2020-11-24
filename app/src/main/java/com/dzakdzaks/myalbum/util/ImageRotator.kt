package com.dzakdzaks.myalbum.util

import android.R.attr.path
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import java.io.IOException


/**
 * ==================================//==================================
 * ==================================//==================================
 * Created on Monday, 16 November 2020 at 11:32 PM.
 * Project Name => MyAlbum
 * Package Name => com.dzakdzaks.myalbum.util
 * ==================================//==================================
 * ==================================//==================================
 */
object ImageRotator {
    @Throws(IOException::class)
    fun rotateImage(path: String?): Bitmap {
        val bitmap = BitmapFactory.decodeFile(path)
        return rotateImage(bitmap, path)
    }

    @Throws(IOException::class)
    fun rotateImage(bitmap: Bitmap, path: String?): Bitmap {
        var rotate = 0
        val exif = ExifInterface(path!!)
        val orientation: Int = exif.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        )
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_270 -> rotate = 270
            ExifInterface.ORIENTATION_ROTATE_180 -> rotate = 180
            ExifInterface.ORIENTATION_ROTATE_90 -> rotate = 90
        }
        val matrix = Matrix()
        matrix.postRotate(rotate.toFloat())
        return Bitmap.createBitmap(
            bitmap, 0, 0, bitmap.width,
            bitmap.height, matrix, true
        )
    }
}