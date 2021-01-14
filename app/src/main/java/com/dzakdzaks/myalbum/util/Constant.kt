package com.dzakdzaks.myalbum.util

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder


/**
 * ==================================//==================================
 * ==================================//==================================
 * Created on Monday, 16 November 2020 at 4:35 PM.
 * Project Name => MyAlbum
 * Package Name => com.dzakdzaks.myalbum.util
 * ==================================//==================================
 * ==================================//==================================
 */

object Constant {

    const val DB_NAME = "album_db"
    const val DB_VERSION = 1

    const val TYPE_IMAGE = "image"
    const val TYPE_VIDEO = "video"

    const val TAKE_CAMERA = "camera"
    const val TAKE_GALLERY = "gallery"

    const val FILE_PROVIDER_AUTHORITY = "com.dzakdzaks.myalbum.fileprovider"

    const val REQUEST_CODE_GO_TO_SETTING = 999
    const val REQUEST_CODE_GO_TO_GALLERY = 100
    const val REQUEST_CODE_GO_TO_PHOTO = 200
    const val REQUEST_CODE_GO_TO_VIDEO = 300
    const val REQUEST_PERMISSIONS_ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION = 222


    fun showConfirmationMaterialDialog(
        activity: Activity,
        title: String,
        desc: String,
        positiveText: String,
        negativeText: String,
        onPositiveClicked: () -> Unit = {}
    ) {
        MaterialAlertDialogBuilder(activity)
            .setTitle(title)
            .setMessage(desc)
            .setNegativeButton(negativeText) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(positiveText) { dialog, _ ->
                onPositiveClicked.invoke()
                dialog.dismiss()
            }
            .show()
    }

    fun openSettings(activity: Activity) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", activity.packageName, null)
        intent.data = uri
        activity.startActivityForResult(intent, REQUEST_CODE_GO_TO_SETTING)
    }

    fun hideSystemUI(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            activity.window.setDecorFitsSystemWindows(false)
            activity.window.insetsController?.let {
                it.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            @Suppress("DEPRECATION")
            activity.window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
        }
    }
}