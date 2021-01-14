package com.dzakdzaks.myalbum.util.ext

import com.dzakdzaks.myalbum.util.Constant
import java.text.SimpleDateFormat
import java.util.*

/**
 * ==================================//==================================
 * ==================================//==================================
 * Created on Monday, 16 November 2020 at 5:00 PM.
 * Project Name => MyAlbum
 * Package Name => com.dzakdzaks.myalbum.util.extension
 * ==================================//==================================
 * ==================================//==================================
 */

fun Date.toStringFormat(
    format: String = "yyyy-MM-dd hh:mm:ss",
    locale: Locale = Locale.getDefault()
): String {
    val formatter = SimpleDateFormat(format, locale)
    return formatter.format(this)
}