package com.almaz.sarafanka.presentation.service

import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.almaz.sarafanka.R
import com.almaz.sarafanka.core.model.Service
import com.almaz.sarafanka.presentation.base.BaseFragment
import com.almaz.sarafanka.utils.extensions.observe
import kotlinx.android.synthetic.main.fragment_service.*

class ServiceFragment : BaseFragment<ServiceViewModel>(ServiceViewModel::class.java) {

    private val servicesAdapter = ServicesAdapter {
        rootActivity.navController.navigate(
            R.id.action_serviceFragment_to_otherProfileFragment,
            bundleOf("profile" to it.owner_id)
        )
    }

    override val layoutId = R.layout.fragment_service

    override fun setupView() {
        rv_services.layoutManager = LinearLayoutManager(context)
        rv_services.adapter = servicesAdapter
        et_search_services.addTextChangedListener {
            servicesAdapter.filter.filter(it)
        }
    }

    override fun subscribe(viewModel: ServiceViewModel) {
        super.subscribe(viewModel)
        observe(viewModel.servicesLiveData, ::bindServices)
    }

    private fun bindServices(services: List<Service>) {
        servicesAdapter.submitGlobalList(services)
        servicesAdapter.notifyDataSetChanged()
    }


}