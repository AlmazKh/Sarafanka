package com.almaz.sarafanka.utils

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.almaz.sarafanka.R
import com.almaz.sarafanka.utils.extensions.loadImageWithCustomCorners
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_loaded_image.view.*

class LoadedImagesAdapter(
    private val imagesLambda: (MutableList<Uri>) -> Unit
) : RecyclerView.Adapter<LoadedImagesAdapter.LoadedImagesViewHolder>() {

    private var images: MutableList<Uri> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = LoadedImagesViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_loaded_image, parent, false)
    )

    override fun onBindViewHolder(holder: LoadedImagesViewHolder, position: Int) {
        holder.bind(images[position])
        holder.itemView.ib_delete_photo.setOnClickListener {
            images.removeAt(position)
            notifyDataSetChanged()
            imagesLambda.invoke(images)
        }
    }

    override fun getItemCount(): Int = images.size

    fun submitList(list: MutableList<Uri>) {
        images = list
    }

    class LoadedImagesViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(uri: Uri) {
            itemView.iv_loaded_photo.loadImageWithCustomCorners(uri.toString(), 10)
        }
    }
}
