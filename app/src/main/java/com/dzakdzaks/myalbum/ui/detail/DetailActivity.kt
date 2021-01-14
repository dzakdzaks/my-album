package com.dzakdzaks.myalbum.ui.detail

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.dzakdzaks.myalbum.R
import com.dzakdzaks.myalbum.data.AlbumEntity
import com.dzakdzaks.myalbum.databinding.ActivityDetailBinding
import com.dzakdzaks.myalbum.util.Constant
import com.dzakdzaks.myalbum.util.ext.gone
import com.dzakdzaks.myalbum.util.ext.visible


class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private lateinit var albumEntity: AlbumEntity
    private lateinit var imageTransitionName: String

    private lateinit var exoPlayerHelper: ExoPlayerHelper

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        Constant.hideSystemUI(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getBundleExtras()
        initialize()
    }

    override fun onResume() {
        super.onResume()
        if (::exoPlayerHelper.isInitialized) {
            exoPlayerHelper.resumePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (::exoPlayerHelper.isInitialized) {
            exoPlayerHelper.pausePlayer()
            if (isFinishing) {
                exoPlayerHelper.killPlayer()
            }
        }
    }

    private fun getBundleExtras() {
        intent.extras?.let {
            albumEntity = it.getParcelable(EXTRA_DATA)!!
            imageTransitionName = it.getString(EXTRA_TRANSITION_NAME)!!
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initialize() {
        if (::albumEntity.isInitialized && ::imageTransitionName.isInitialized) {
            if (albumEntity.type == Constant.TYPE_IMAGE) {
                ViewCompat.setTransitionName(binding.photoView, imageTransitionName)

                binding.apply {
                    exoPlayerView.gone()
                    photoView.visible()

                    loadImage(albumEntity)

                    iconBack.setOnClickListener { onBackPressed() }
                }

            } else if (albumEntity.type == Constant.TYPE_VIDEO) {
                ViewCompat.setTransitionName(binding.exoPlayerView, imageTransitionName)

                binding.apply {
                    iconBack.gone()
                    photoView.gone()
                    exoPlayerView.visible()

                    loadVideo(albumEntity)
                }

                /*video custom controller*/
                val tvHeader = findViewById<View>(R.id.header_tv) as AppCompatTextView
                val toggleInfo = findViewById<View>(R.id.toggleInfo_im) as ImageButton
                val btnCloseVideo = findViewById<View>(R.id.exo_close) as ImageButton

                tvHeader.text = "Video ${albumEntity.id}"
                toggleInfo.setOnClickListener { /*todo info media*/ Toast.makeText(
                    applicationContext,
                    "Media Info Video",
                    Toast.LENGTH_SHORT
                ).show()
                }
                btnCloseVideo.setOnClickListener { onBackPressed() }
            }
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
        binding.exoPlayerView.keepScreenOn
        exoPlayerHelper = ExoPlayerHelper(binding.exoPlayerView, {
            Toast.makeText(applicationContext, it.localizedMessage, Toast.LENGTH_SHORT).show()
        }, { isBuffering ->
            if (isBuffering)
                binding.progressBar.visibility = View.VISIBLE
            else
                binding.progressBar.visibility = View.GONE
        })

        exoPlayerHelper.initializePlayer(entity.url)
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

}