package com.pibm.tablenew.fixestable.widget

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import com.pibm.tablenew.R

object TextViewUtils {
    fun generateTextView(
            context: Context?, text: String?, gravity: Int, minWidth: Int, padding: Int): TextView {
        val textView = TextView(context)
        //textView.setTextAppearance(context, R.style.textViewStyle);
        textView.setTextAppearance(R.style.textViewStyle)
        setTextView(textView, text, gravity, minWidth, padding)
        return textView
    }

    fun setTextView(
            textView: TextView, text: String?, gravity: Int, minWidth: Int, padding: Int) {
        textView.text = text
        textView.gravity = gravity
        textView.minWidth = minWidth
        textView.setPadding(0, padding / 2, 0, padding / 2)
    }

    fun generateTitleTextView(
            context: Context?, text: String?, gravity: Int, minWidth: Int, padding: Int): TextView {
        val textView = TextView(context)
        //textView.setTextAppearance(context, R.style.textViewStyle);
        textView.setTextAppearance(R.style.textViewTitleStyle)
        setTitleTextView(textView, text, gravity, minWidth, padding)
        return textView
    }

    fun setTitleTextView(
            textView: TextView, text: String?, gravity: Int, minWidth: Int, padding: Int) {
        textView.text = text
        textView.gravity = gravity
        textView.minWidth = minWidth
        textView.setPadding(0, padding / 2, 0, padding / 2)
    }

    fun generateImageView(
            context: Context?, src: Int, gravity: Int, maxHeight: Int, padding: Int): ImageView {
        val imageView = ImageView(context)
        imageView.maxHeight = 30
        setImageView(imageView, src, gravity, maxHeight, padding)
        return imageView
    }

    fun setImageView(
            imageView: ImageView, src: Int, gravity: Int, maxHeight: Int, padding: Int) {
        imageView.setImageResource(src)
        imageView.setPadding(padding/3, padding / 3, padding/3, padding / 3)
    }
}