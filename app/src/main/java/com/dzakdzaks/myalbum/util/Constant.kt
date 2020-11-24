package com.dzakdzaks.myalbum.util

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
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

}