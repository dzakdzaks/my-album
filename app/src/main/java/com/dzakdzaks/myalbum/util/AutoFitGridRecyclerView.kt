package com.dzakdzaks.myalbum.util

import android.R
import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView


/**
 * ==================================//==================================
 * ==================================//==================================
 * Created on Tuesday, 17 November 2020 at 12:57 AM.
 * Project Name => MyAlbum
 * Package Name => com.dzakdzaks.myalbum.util
 * ==================================//==================================
 * ==================================//==================================
 */
class AutoFitGridRecyclerView : RecyclerView {
    private var manager: GridLayoutManager? = null
    private var columnWidth = -1

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        if (attrs != null) {
            // list the attributes we want to fetch
            // columnWidth is what GridView uses, so we use it too
            val attrsArray = intArrayOf(
                R.attr.columnWidth
            )
            val array: TypedArray = context.obtainStyledAttributes(attrs, attrsArray)

            //retrieve the value of the 0 index, which is columnWidth
            columnWidth = array.getDimensionPixelSize(0, -1)
            array.recycle()
        }
        manager = GridLayoutManager(context, 1)
        layoutManager = manager
    }

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        super.onMeasure(widthSpec, heightSpec)
        if (columnWidth > 0) {
            //The spanCount will always be at least 1
            val spanCount = Math.max(1, measuredWidth / columnWidth)
            manager!!.spanCount = spanCount
        }
    }

}