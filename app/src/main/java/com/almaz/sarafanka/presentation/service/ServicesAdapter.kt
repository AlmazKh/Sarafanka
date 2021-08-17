package com.almaz.sarafanka.presentation.service

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.almaz.sarafanka.R
import com.almaz.sarafanka.core.model.Service
import com.almaz.sarafanka.utils.extensions.getDownloadablePhotoUrl
import com.almaz.sarafanka.utils.extensions.loadImageWithCustomCorners
import com.almaz.sarafanka.utils.extensions.toInvisible
import com.almaz.sarafanka.utils.extensions.toVisible
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_profile_services.view.*
import java.util.*

class ServicesAdapter(private val serviceLambda: (Service) -> Unit) :
    ListAdapter<Service, ServicesAdapter.ServiceViewHolder>(ServiceDiffCallback()), Filterable {

    private var globalList: List<Service> = listOf()

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
            if (service.reviews.isNullOrEmpty())
                itemView.tv_has_examples.toInvisible()
            else
                itemView.tv_has_examples.toVisible()
            if (service.photo.isNullOrEmpty())
                itemView.iv_service_avatar.loadImageWithCustomCorners(R.drawable.ic_no_photo, 15)
            else
                itemView.iv_service_avatar.loadImageWithCustomCorners(FirebaseStorage.getInstance().getDownloadablePhotoUrl(service.photo.first()), 15)
        }
    }

    class ServiceDiffCallback : DiffUtil.ItemCallback<Service>() {
        override fun areItemsTheSame(oldItem: Service, newItem: Service): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: Service, newItem: Service): Boolean =
            oldItem == newItem
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                val filterResults = FilterResults()
                return if (charSearch.isNotBlank()) {
                    filterResults.values = globalList.filter { service ->
                        service.category.name.toLowerCase(Locale.ROOT)
                            .contains(charSearch.toLowerCase(Locale.ROOT)) ||
                                service.subcategory.toLowerCase(Locale.ROOT)
                                    .contains(charSearch.toLowerCase(Locale.ROOT))
                    }
                    filterResults
                } else {
                    filterResults.values = globalList
                    filterResults
                }
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                submitList(results?.values as List<Service>)
                notifyDataSetChanged()
            }
        }
    }

    fun submitGlobalList(list: List<Service>) {
        submitList(list)
        globalList = list
    }
}
