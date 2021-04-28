package com.dzakdzaks.myalbum.ui.camerax

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.dzakdzaks.myalbum.R
import com.dzakdzaks.myalbum.databinding.ActivityCameraXBinding
import id.dipay.camerax.CameraUtil
import id.dipay.camerax.Selector
import java.io.File

class CameraXActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraXBinding

    private val vm by viewModels<CameraXViewModel>()

    private val cameraUtil: CameraUtil by lazy {
        CameraUtil(this, this, binding.viewFinder, getOutputDirectory())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCameraXBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (allPermissionsGranted()) {
            cameraUtil.startCamera()
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        binding.apply {
            cameraCaptureButton.setOnClickListener {
                cameraUtil.takePhoto {
                    val msg = "Photo capture succeeded: $it"
                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                    Log.d(CameraUtil.TAG, msg)
                }
            }

            btnTorch.setOnClickListener {
                cameraUtil.toggleTorch()
            }

            btnMirror.setOnClickListener {
                cameraUtil.flipCamera { selector ->
                    vm.setCameraSelector(selector)
                }
            }
        }

        observeLiveData()
    }

    private fun getOutputDirectory(): File {
        val mediaDir = this.externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply {
                mkdirs()
            }
        }

        return if (mediaDir != null && mediaDir.exists()) mediaDir else filesDir
    }

    private fun observeLiveData() {
        vm.cameraSelector.observe(this) {
            binding.apply {
                when (it) {
                    Selector.FRONT -> {
                        identityFrame.visibility = View.GONE
                        personFrame.visibility = View.VISIBLE
                    }

                    Selector.BACK -> {
                        personFrame.visibility = View.GONE
                        identityFrame.visibility = View.VISIBLE
                    }

                    else -> {
                        personFrame.visibility = View.GONE
                        identityFrame.visibility = View.VISIBLE
                    }
                }
            }
        }
    }


    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                cameraUtil.startCamera()
            } else {
                Toast.makeText(this, "Permission not granted by the user.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}