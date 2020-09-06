package com.almaz.sarafanka.presentation.profile

import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.almaz.sarafanka.R
import com.almaz.sarafanka.core.model.Service
import com.almaz.sarafanka.core.model.User
import com.almaz.sarafanka.presentation.base.BaseFragment
import com.almaz.sarafanka.utils.extensions.loadCircleImage
import com.almaz.sarafanka.utils.extensions.observe
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : BaseFragment<ProfileViewModel>(ProfileViewModel::class.java) {
    private val servicesAdapter = ProfileServicesAdapter {
        rootActivity.navController.navigate(
            R.id.action_profileFragment_to_serviceDetailsFragment,
            bundleOf("service" to it)
        )
    }
    override val layoutId: Int = R.layout.fragment_profile

    override fun setupView() {
        rv_profile_services.layoutManager = LinearLayoutManager(context)
        rv_profile_services.adapter = servicesAdapter
    }

    override fun subscribe(viewModel: ProfileViewModel) {
        super.subscribe(viewModel)
        observe(viewModel.profileInfoLiveData, ::bindProfileInfo)
        observe(viewModel.profileServicesLiveData, ::bindProfileServices)
    }

    private fun bindProfileInfo(user: User) {
        tv_user_name.text = user.name
        user.photo?.let { iv_user_avatar.loadCircleImage(it) }
    }

    private fun bindProfileServices(services: List<Service>) {
        servicesAdapter.submitList(services)
        servicesAdapter.notifyDataSetChanged()
    }
}
