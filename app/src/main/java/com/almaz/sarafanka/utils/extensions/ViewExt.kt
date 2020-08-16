package com.almaz.sarafanka.utils.extensions

import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide

fun View.toVisible() {
    this.visibility = View.VISIBLE
}

fun View.toGone() {
    this.visibility = View.GONE
}

fun View.toInvisible() {
    this.visibility = View.INVISIBLE
}

fun ImageView.loadImage(@DrawableRes resId: Int) =
    Glide.with(this)
        .load(resId)
        .into(this)

fun ImageView.loadImage(url: String) =
    Glide.with(this)
        .load(url)
        .into(this)

fun ImageView.loadCircleImage(url: String) =
    Glide.with(this)
        .load(url)
        .centerCrop()
        .circleCrop()
        .into(this)

fun ImageView.loadCircleImage(@DrawableRes resId: Int) =
    Glide.with(this)
        .load(resId)
        .centerCrop()
        .circleCrop()
        .into(this)
