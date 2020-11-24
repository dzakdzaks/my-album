package com.dzakdzaks.myalbum.ui.detail

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.MediaController
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.dzakdzaks.myalbum.data.AlbumEntity
import com.dzakdzaks.myalbum.databinding.ActivityDetailBinding
import com.dzakdzaks.myalbum.util.Constant
import com.dzakdzaks.myalbum.util.CustomVideoView
import com.dzakdzaks.myalbum.util.ext.gone
import com.dzakdzaks.myalbum.util.ext.visible


class DetailActivity : AppCompatActivity(), CustomVideoView.PlayPauseListener {

    private lateinit var binding: ActivityDetailBinding

    private lateinit var albumEntity: AlbumEntity
    private lateinit var imageTransitionName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getBundleExtras()
        initialize()
        clickable()
    }

    private fun getBundleExtras() {
        intent.extras?.let {
            albumEntity = it.getParcelable(EXTRA_DATA)!!
            imageTransitionName = it.getString(EXTRA_TRANSITION_NAME)!!
        }
    }

    private fun initialize() {
        if (::albumEntity.isInitialized && ::imageTransitionName.isInitialized) {
            if (albumEntity.type == Constant.TYPE_IMAGE) {
                ViewCompat.setTransitionName(binding.photoView, imageTransitionName)
                binding.videoView.gone()
                binding.photoView.visible()
                loadImage(albumEntity)
            } else if (albumEntity.type == Constant.TYPE_VIDEO) {
                ViewCompat.setTransitionName(binding.videoView, imageTransitionName)
                binding.photoView.gone()
                binding.videoView.visible()
                loadVideo(albumEntity)
            }
        }
    }

    private fun clickable() {
        binding.iconBack.setOnClickListener {
            onBackPressed()
        }
        binding.iconPlay.setOnClickListener {
            binding.videoView.start()
        }
    }

    private fun loadImage(entity: AlbumEntity) {
        Glide.with(binding.photoView.context)
            .load(entity.url)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    supportStartPostponedEnterTransition()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    supportStartPostponedEnterTransition()
                    return false
                }

            })
            .into(binding.photoView)
    }

    private fun loadVideo(entity: AlbumEntity) {
        val uri: Uri = Uri.parse(entity.url)
        val mediaController = MediaController(this)

        binding.videoView.setPlayPauseListener(this)
        binding.videoView.setVideoURI(uri)
        binding.videoView.setMediaController(mediaController)
        mediaController.setAnchorView(binding.videoView)

        val videoWidth: Int = binding.videoView.width
        val videoHeight: Int = binding.videoView.height
        val videoProportion = videoWidth.toFloat() / videoHeight.toFloat()
        val screenWidth = windowManager.defaultDisplay.width
        val screenHeight = windowManager.defaultDisplay.height
        val screenProportion = screenWidth.toFloat() / screenHeight.toFloat()
        val lp = binding.videoView.layoutParams

        if (videoProportion > screenProportion) {
            lp.width = screenWidth
            lp.height = (screenWidth.toFloat() / videoProportion).toInt()
        } else {
            lp.width = (videoProportion * screenHeight.toFloat()).toInt()
            lp.height = screenHeight
        }
        binding.videoView.layoutParams = lp
        binding.videoView.start()
    }

    override fun onResume() {
        super.onResume()
        binding.iconPlay.visible()
    }

    companion object {
        const val EXTRA_DATA = "EXTRA_DATA"
        const val EXTRA_TRANSITION_NAME = "EXTRA_TRANSITION_NAME"

        fun instance(activity: Activity, albumEntity: AlbumEntity, view: View) {
            val intent = Intent(activity, DetailActivity::class.java)
            intent.putExtra(EXTRA_DATA, albumEntity)
            intent.putExtra(EXTRA_TRANSITION_NAME, ViewCompat.getTransitionName(view))

            val options: ActivityOptionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    activity,
                    view,
                    ViewCompat.getTransitionName(view).toString()
                )

            activity.startActivity(intent, options.toBundle())
        }
    }

    override fun onPlayVideo() {
        supportStartPostponedEnterTransition()
        binding.iconPlay.gone()
    }

    override fun onPauseVideo() {
        supportStartPostponedEnterTransition()
        binding.iconPlay.visible()
    }


}