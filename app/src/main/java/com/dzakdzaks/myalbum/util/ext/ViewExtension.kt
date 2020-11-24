package com.dzakdzaks.myalbum.util.ext

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import androidx.core.view.isVisible


/**
 * ==================================//==================================
 * ==================================//==================================
 * Created on Monday, 16 November 2020 at 3:29 PM.
 * Project Name => MyAlbum
 * Package Name => com.dzakdzaks.myalbum.util
 * ==================================//==================================
 * ==================================//==================================
 */

fun View.rotateView(rotate: Boolean): Boolean {
    animate().setDuration(200)
        .setListener(object : AnimatorListenerAdapter() {
        })
        .rotation(if (rotate) 135f else 0f)
    return rotate
}

fun View.showWithAnim() {
    visibility = View.VISIBLE
    alpha = 0f
    translationY = height.toFloat()
    animate()
        .setDuration(200)
        .translationY(0f)
        .setListener(object : AnimatorListenerAdapter() {
        })
        .alpha(1f)
        .start()
}

fun View.hideWithAnim() {
    visibility = View.VISIBLE
    alpha = 1f
    translationY = 0f
    animate()
        .setDuration(200)
        .translationY(height.toFloat())
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                visibility = View.GONE
                super.onAnimationEnd(animation)
            }
        }).alpha(0f)
        .start()
}

fun View.initFab() {
    visibility = View.GONE
    translationY = height.toFloat()
    alpha = 0f
}

fun View.visible() {
    if (!this.isVisible)
        this.visibility = View.VISIBLE
}

fun View.gone() {
    if (this.isVisible)
        this.visibility = View.GONE
}
