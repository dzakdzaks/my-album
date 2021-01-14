package com.dzakdzaks.myalbum.ui.detail

import com.dzakdzaks.myalbum.R
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util


class ExoPlayerHelper(
    private val playerView: PlayerView,
    onError: (ExoPlaybackException) -> Unit,
    onPlayerBuffer: (Boolean) -> Unit
) {

    private var exoPlayer: ExoPlayer? = null
    private var mediaSource: ProgressiveMediaSource? = null

    private val playerListener = object : Player.EventListener {
        override fun onPlayerError(error: ExoPlaybackException) {
            super.onPlayerError(error)
            onError(error)
        }

        override fun onPlaybackStateChanged(state: Int) {
            super.onPlaybackStateChanged(state)
            onPlayerBuffer(state == Player.STATE_BUFFERING)
        }
    }

    fun initializePlayer(url: String) {
        exoPlayer = SimpleExoPlayer.Builder(playerView.context).build()
        exoPlayer?.repeatMode = Player.REPEAT_MODE_OFF
        exoPlayer?.addListener(playerListener)

        playerView.player = exoPlayer

        val extractorsFactory = DefaultExtractorsFactory()
        extractorsFactory.setConstantBitrateSeekingEnabled(true)

        val userAgent =
            Util.getUserAgent(
                playerView.context,
                playerView.context.getString(R.string.app_name)
            )

        mediaSource = ProgressiveMediaSource
            .Factory(
                DefaultDataSourceFactory(playerView.context, userAgent),
                extractorsFactory
            )
            .createMediaSource(MediaItem.fromUri(url))

        exoPlayer?.setMediaSource(mediaSource!!, true)
        exoPlayer?.prepare()
        exoPlayer?.playWhenReady = true
    }

    fun resumePlayer() {
        exoPlayer?.playWhenReady = true
    }

    fun pausePlayer() {
        exoPlayer?.playWhenReady = false
    }

    fun killPlayer() {
        if (exoPlayer != null) {
            exoPlayer?.release()
            exoPlayer = null
            mediaSource = null
            playerView.player = null
        }
    }
}