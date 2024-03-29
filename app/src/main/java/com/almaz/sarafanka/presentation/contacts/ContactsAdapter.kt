package com.almaz.sarafanka.presentation.contacts

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.almaz.sarafanka.R
import com.almaz.sarafanka.core.model.Contact
import kotlinx.android.synthetic.main.row_contact.view.*
import kotlinx.android.synthetic.main.row_contact_data.view.*
import java.util.*
import kotlin.reflect.KFunction1

class ContactsAdapter(
    context: Context,
    val onClick: KFunction1<@ParameterName(name = "contact") Contact, Unit>
) : RecyclerView.Adapter<ContactsAdapter.MyViewHolder>(), Filterable {
    val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater


    var globalList = listOf<Contact>()
    var contacts = listOf<Contact>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(layoutInflater.inflate(R.layout.row_contact, parent, false))
    }

    override fun getItemCount() = contacts.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val contact = contacts[position]
        with(holder.itemView) {
            setOnClickListener {
                onClick.invoke(contact)
            }
            tvContactName.text = contact.name
            llContactDetails.removeAllViews()
            tvContactInvite.text = if( contact.user == null ) "Пригласить" else "Профиль"
            tvContactInvite.setTextColor(resources.getColor(if( contact.user == null)R.color.colorGreen else R.color.colorPrimary))
            contact.numbers
                    .forEach {
                val detail = layoutInflater.inflate(R.layout.row_contact_data,llContactDetails,false)
                detail.tvContactData.text = it
                llContactDetails.addView(detail)
            }
            contact.emails.forEach {
                val detail = layoutInflater.inflate(R.layout.row_contact_data,llContactDetails,false)
                detail.tvContactData.text = it
                llContactDetails.addView(detail)
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                val filterResults = FilterResults()
                return if (charSearch.isNotBlank()) {
                    filterResults.values = globalList?.filter { badge ->
                        badge.name.toLowerCase(Locale.ROOT)
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
                contacts = (results?.values as List<Contact>)
                notifyDataSetChanged()
            }
        }
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    fun submitGlobalList(list: List<Contact>) {
        contacts = list
        globalList = list
    }
}
