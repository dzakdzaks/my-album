package com.dzakdzaks.myalbum.ui.draw

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dzakdzaks.myalbum.R
import com.dzakdzaks.myalbum.databinding.ActivityDrawingBinding

class DrawingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDrawingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDrawingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            fabClear.setOnClickListener {
                drawingCanvas.clear()
            }
        }
    }
}