package com.almaz.sarafanka.presentation.service

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.almaz.sarafanka.R
import com.almaz.sarafanka.core.model.Review
import com.almaz.sarafanka.utils.extensions.loadCircleImage
import com.almaz.sarafanka.utils.extensions.toGone
import com.almaz.sarafanka.utils.extensions.toVisible
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_service_reviews.view.*

class ServiceReviewsAdapter :
    ListAdapter<Review, ServiceReviewsAdapter.ReviewsViewHolder>(ReviewsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewsViewHolder =
        ReviewsViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_service_reviews, parent, false)
        )

    override fun onBindViewHolder(holder: ReviewsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ReviewsViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(review: Review) {
            review.writer.photo?.let { itemView.iv_user_avatar.loadCircleImage(it) }
            itemView.tv_user_name.text = review.writer.name
            when (review.isRecommended) {
                true -> itemView.tv_recommended.toVisible()
                else -> itemView.tv_recommended.toGone()
            }
            when (review.inContacts) {
                true -> itemView.tv_in_contacts.toVisible()
                else -> itemView.tv_in_contacts.toGone()
            }
            itemView.tv_review_description.text = review.description
            with(itemView.rv_review_image_list) {
                layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                adapter = review.photo?.let { ImageListAdapter(it) }
            }
            itemView.tv_price.text = "Цена: ${review.service_price} руб."
        }
    }

    class ReviewsDiffCallback : DiffUtil.ItemCallback<Review>() {
        override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean =
            oldItem == newItem
    }
}
