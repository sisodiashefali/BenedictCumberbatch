package com.android.benedictcumberbatchapp.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.android.benedictcumberbatchapp.R
import com.bumptech.glide.Glide

@BindingAdapter("textOrDefault")
fun setTextOrDefault(textView: TextView, text: String?) {
    textView.text = text ?: ""
}

@BindingAdapter("textYear")
fun setDate(textView: TextView, text: String?) {
    textView.text = "Year : $text" ?: ""
}

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, url: String?) {
    val posterUrl = "https://image.tmdb.org/t/p/w500${url}"
     Glide.with(view.context)
        .load(posterUrl).error(android.R.drawable.ic_menu_report_image)
        .into(view)
}