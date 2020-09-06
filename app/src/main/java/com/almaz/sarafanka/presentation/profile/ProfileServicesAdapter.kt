package com.almaz.sarafanka.presentation.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.almaz.sarafanka.R
import com.almaz.sarafanka.core.model.Service
import com.almaz.sarafanka.utils.extensions.loadCircleImage
import com.almaz.sarafanka.utils.extensions.loadImageWithCustomCorners
import com.almaz.sarafanka.utils.extensions.toInvisible
import com.almaz.sarafanka.utils.extensions.toVisible
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_profile_services.view.*

class ProfileServicesAdapter(private val serviceLambda: (Service) -> Unit) :
    ListAdapter<Service, ProfileServicesAdapter.ServiceViewHolder>(ServiceDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder =
        ServiceViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_profile_services, parent, false)
        )

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            serviceLambda.invoke(getItem(position))
        }
    }

    class ServiceViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(service: Service) {
            itemView.tv_service_category.text = service.category.name
            itemView.tv_service_subcategory.text = service.subcategory
            itemView.tv_service_description.text = service.description
            if (service.reviews == null)
                itemView.tv_has_examples.toInvisible()
            else
                itemView.tv_has_examples.toVisible()
            if (service.photo == null)
                itemView.iv_service_avatar.loadImageWithCustomCorners(R.drawable.ic_default_avatar, 15)
            else
                itemView.iv_service_avatar.loadImageWithCustomCorners(service.photo.first(), 15)
        }
    }

    class ServiceDiffCallback : DiffUtil.ItemCallback<Service>() {
        override fun areItemsTheSame(oldItem: Service, newItem: Service): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: Service, newItem: Service): Boolean =
            oldItem == newItem
    }
}
