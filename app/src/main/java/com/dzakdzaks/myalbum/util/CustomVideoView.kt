package com.dzakdzaks.myalbum.util

import android.content.Context
import android.util.AttributeSet
import android.widget.VideoView

/**
 * ==================================//==================================
 * ==================================//==================================
 * Created on Tuesday, 17 November 2020 at 12:30 PM.
 * Project Name => MyAlbum
 * Package Name => com.dzakdzaks.myalbum.util
 * ==================================//==================================
 * ==================================//==================================
 */
class CustomVideoView : VideoView {
    private var mListener: PlayPauseListener? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )

    fun setPlayPauseListener(listener: PlayPauseListener?) {
        mListener = listener
    }

    override fun pause() {
        super.pause()
        if (mListener != null) {
            mListener!!.onPauseVideo()
        }
    }

    override fun start() {
        super.start()
        if (mListener != null) {
            mListener!!.onPlayVideo()
        }
    }

    interface PlayPauseListener {
        fun onPlayVideo()
        fun onPauseVideo()
    }
}