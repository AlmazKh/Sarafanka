package com.almaz.sarafanka.utils.extensions

import android.app.Service
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.almaz.sarafanka.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.firebase.storage.StorageReference

fun View.hideKeyboard() {
    (context?.getSystemService(Service.INPUT_METHOD_SERVICE) as? InputMethodManager)
        ?.hideSoftInputFromWindow(this.windowToken, 0)
}

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

fun ImageView.loadCircleImage(reference: StorageReference) =
    Glide.with(this)
        .load(reference)
        .centerCrop()
        .circleCrop()
        .error(R.drawable.ic_default_avatar)
        .into(this)

fun ImageView.loadImageWithCustomCorners(@DrawableRes resId: Int, radius: Int) =
    Glide.with(this)
        .load(resId)
        .transform(CenterCrop(), RoundedCorners(radius))
        .into(this)

fun ImageView.loadImageWithCustomCorners(reference: StorageReference, radius: Int) =
    Glide.with(this)
        .load(reference)
        .transform(CenterCrop(), RoundedCorners(radius))
        .into(this)
