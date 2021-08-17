package com.almaz.sarafanka.presentation.service

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.almaz.sarafanka.R
import com.almaz.sarafanka.utils.extensions.loadImageWithCustomCorners
import com.google.firebase.storage.StorageReference

class ImageListAdapter(
    private var images: List<StorageReference>
) : RecyclerView.Adapter<ImageListAdapter.ImagesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_image_list, parent, false)
        return ImagesViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImagesViewHolder, position: Int) {
        holder.imageView.loadImageWithCustomCorners(images[position], 50)
    }

    override fun getItemCount(): Int = images.size

    fun submitList(list: List<StorageReference>) {
        images = list
    }

    class ImagesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.iv_photo_for_image_list)
    }
}
