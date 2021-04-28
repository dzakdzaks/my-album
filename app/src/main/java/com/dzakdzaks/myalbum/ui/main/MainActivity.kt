package com.dzakdzaks.myalbum.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.dzakdzaks.myalbum.R
import com.dzakdzaks.myalbum.databinding.ActivityMainBinding
import com.dzakdzaks.myalbum.ui.camerax.CameraXActivity
import com.dzakdzaks.myalbum.ui.detail.DetailActivity
import com.dzakdzaks.myalbum.ui.draw.DrawingActivity
import com.dzakdzaks.myalbum.util.BitmapUtils
import com.dzakdzaks.myalbum.util.Constant
import com.dzakdzaks.myalbum.util.ext.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels()

    private lateinit var mainAdapter: MainAdapter

    private var isRotated: Boolean = false
    private lateinit var isFromWhere: String

    private lateinit var currentMediaPath: String
    private lateinit var currentMediaBitmap: Bitmap


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*todo animation with View.animate*/
        /*initialize()*/
        /*todo animation with View.animate*/

        setupAlbumList()
        observeAllData()
        clickable()
    }

    private fun clickable() {
        binding.fabAdd.setOnClickListener {
            isRotated = if (!isRotated) {
                transitionFab(R.id.start, R.id.end)
                true
            } else {
                transitionFab(R.id.end, R.id.start)
                false
            }
        }

        binding.fabAdd.setOnLongClickListener {
            if (isRotated) {
                startActivity(Intent(this, CameraXActivity::class.java))
            } else {
                startActivity(Intent(this, DrawingActivity::class.java))
            }
            false
        }

        binding.fabCamera.setOnClickListener {
            permissionCheckWhenClicked(Constant.TAKE_CAMERA)
        }

        binding.fabGallery.setOnClickListener {
            permissionCheckWhenClicked(Constant.TAKE_GALLERY)
        }

    }

    private fun transitionFab(start: Int, end: Int, duration: Int = 300) {
        binding.motionLayout.setTransition(start, end)
        binding.motionLayout.setTransitionDuration(duration)
        binding.motionLayout.transitionToEnd()
    }

    private fun permissionCheckWhenClicked(isFromWhereValue: String) {
        transitionFab(R.id.end, R.id.start)
        isRotated = false
        isFromWhere = isFromWhereValue
        permissionCheck()
    }

    /*todo animation with View.animate*/
    /*private fun initialize() {
        binding.fabCamera.initFab()
        binding.fabGallery.initFab()
        clickable()
    }

    private fun clickable() {
        binding.fabAdd.setOnClickListener {
            isRotate = it.rotateView(!isRotate)
            if (isRotate) {
                binding.fabCamera.showWithAnim()
                binding.fabGallery.showWithAnim()
            } else {
                binding.fabCamera.hideWithAnim()
                binding.fabGallery.hideWithAnim()
            }
        }

        binding.fabCamera.setOnClickListener {
            closeFab()
            isFromWhere = Constant.TAKE_CAMERA
            permissionCheck(isFromWhere)
        }

        binding.fabGallery.setOnClickListener {
            closeFab()
            isFromWhere = Constant.TAKE_GALLERY
            permissionCheck(isFromWhere)
        }
    }

    private fun closeFab() {
        isRotate = binding.fabAdd.rotateView(!isRotate)
        binding.fabCamera.hideWithAnim()
        binding.fabGallery.hideWithAnim()
    }*/
    /*todo animation with View.animate*/

    private fun permissionCheck() {
        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    // permissions granted
                    if (report.areAllPermissionsGranted()) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                            if (Environment.isExternalStorageManager()) {
                                toCameraOrGallery()
                            } else {
                                intentToRequestManageAllFileAccess()
                            }
                        } else {
                            toCameraOrGallery()
                        }
                    }

                    // permanent denial of permission
                    if (report.isAnyPermissionPermanentlyDenied) {
                        Constant.showConfirmationMaterialDialog(
                            this@MainActivity,
                            getString(R.string.app_need_permission),
                            getString(R.string.app_need_permission_msg),
                            getString(R.string.yes),
                            getString(R.string.no),
                            onPositiveClicked = { Constant.openSettings(this@MainActivity) }
                        )
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest?>?,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).withErrorListener { error ->
                Toast.makeText(
                    applicationContext,
                    getString(R.string.error_req_permission, error),
                    Toast.LENGTH_SHORT
                ).show()
            }
            .onSameThread()
            .check()
    }

    private fun toCameraOrGallery() {
        if (isFromWhere == Constant.TAKE_CAMERA)
            showChooserCamera()
        else
            goToGallery()
    }

    private fun showChooserCamera() {
        val items = arrayOf("Photo", "Video")

        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.choose))
            .setItems(items) { dialog, which ->
                when (which) {
                    0 -> takePhoto()
                    1 -> takeVideo()
                }
                dialog.dismiss()
            }
            .show()
    }

    private fun goToGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "*/*"
        startActivityForResult(intent, Constant.REQUEST_CODE_GO_TO_GALLERY)
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun takePhoto() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                var photoFile: File? = null
                try {
                    photoFile = BitmapUtils.createTempImageFile(this)
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    Toast.makeText(this, ex.message, Toast.LENGTH_SHORT).show()
                }
                photoFile?.also {
                    currentMediaPath = it.absolutePath
                    Log.d("currentMediaPath", currentMediaPath)

                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        Constant.FILE_PROVIDER_AUTHORITY,
                        photoFile
                    )

                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    try {
                        startActivityForResult(takePictureIntent, Constant.REQUEST_CODE_GO_TO_PHOTO)
                    } catch (e: ActivityNotFoundException) {
                        Toast.makeText(this, "ERROR ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun takeVideo() {
        Intent(MediaStore.ACTION_VIDEO_CAPTURE).also { takeVideoIntent ->
            takeVideoIntent.resolveActivity(packageManager)?.also {
                try {
                    startActivityForResult(takeVideoIntent, Constant.REQUEST_CODE_GO_TO_VIDEO)
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(this, "ERROR ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Constant.REQUEST_CODE_GO_TO_SETTING -> {
                if (resultCode == RESULT_OK)
                    permissionCheck()
            }
            Constant.REQUEST_CODE_GO_TO_GALLERY, Constant.REQUEST_CODE_GO_TO_VIDEO -> {
                if (resultCode == RESULT_OK) {
                    val selectedMediaUri = data!!.data
                    when {
                        selectedMediaUri.toString().contains("image") -> insertMediaData(
                            data.data,
                            Constant.TYPE_IMAGE
                        )
                        selectedMediaUri.toString().contains("video") -> insertMediaData(
                            data.data,
                            Constant.TYPE_VIDEO
                        )
                        else -> insertMediaData(data.data, Constant.TYPE_VIDEO)
                    }
                }
            }
            Constant.REQUEST_CODE_GO_TO_PHOTO -> {
                if (resultCode == RESULT_OK)
                    saveImageToGallery()
                else
                    lifecycleScope.launch(Dispatchers.IO) {
                        if (::currentMediaPath.isInitialized)
                            BitmapUtils.deleteImageFile(this@MainActivity, currentMediaPath)
                    }
            }
            Constant.REQUEST_PERMISSIONS_ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    if (Environment.isExternalStorageManager()) {
                        toCameraOrGallery()
                    } else {
                        Snackbar.make(
                            binding.root,
                            "Need permission to allow access storage",
                            Snackbar.LENGTH_INDEFINITE
                        )
                            .setAction("Allow") { intentToRequestManageAllFileAccess() }.show()
                    }
                } else {
                    toCameraOrGallery()
                }
            }
        }
    }

    private fun saveImageToGallery() {
        lifecycleScope.launch(Dispatchers.IO) {
            if (::currentMediaPath.isInitialized) {
                currentMediaBitmap = BitmapUtils.resamplePic(this@MainActivity, currentMediaPath)
                val rotatedImage = BitmapUtils.getRotateImage(currentMediaPath, currentMediaBitmap)
                BitmapUtils.deleteImageFile(this@MainActivity, currentMediaPath)
                val savedImagePath = BitmapUtils.saveImage(this@MainActivity, rotatedImage) ?: ""
                if (savedImagePath != "")
                    viewModel.insert(savedImagePath, Constant.TYPE_IMAGE)
                else
                    Toast.makeText(applicationContext, "File not found.", Toast.LENGTH_SHORT)
                        .show()
            }
        }
    }

    private fun insertMediaData(uri: Uri?, type: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            uri?.let {
                val path: String? = BitmapUtils.getRealPathFromURI(
                    it,
                    this@MainActivity
                )
                if (path != null && path != "") {
                    viewModel.insert(path, type)
                }
            }

        }
    }


    private fun setupAlbumList() {
        mainAdapter = MainAdapter(
            onLongClickItem = {
                Constant.showConfirmationMaterialDialog(
                    this@MainActivity,
                    getString(R.string.delete_confirm_title),
                    getString(R.string.delete_confirm_msg),
                    getString(R.string.yes),
                    getString(R.string.no),
                    onPositiveClicked = {
                        lifecycleScope.launch(Dispatchers.IO) {
                            viewModel.delete(
                                it
                            )
                        }
                    }
                )
            },
            onClickItem = { albumEntity, view -> DetailActivity.instance(this, albumEntity, view) }
        )
        binding.rv.apply {
            adapter = mainAdapter
        }
    }

    private fun observeAllData() {
        lifecycleScope.launch {
            viewModel.getAllData().collect {
                if (it.isEmpty()) {
                    binding.rv.gone()
                    binding.tvNoData.visible()
                } else {
                    binding.tvNoData.gone()
                    binding.rv.visible()
                    mainAdapter.submitList(it)
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private fun intentToRequestManageAllFileAccess() {
        try {
            val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
            val uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            startActivityForResult(
                intent,
                Constant.REQUEST_PERMISSIONS_ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
            )
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(applicationContext, "Activity not found", Toast.LENGTH_SHORT)
                .show()
        }
    }

}