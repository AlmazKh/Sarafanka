package com.almaz.sarafanka.presentation.profile

import android.content.Intent
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.almaz.sarafanka.R
import com.almaz.sarafanka.core.model.Service
import com.almaz.sarafanka.core.model.User
import com.almaz.sarafanka.presentation.base.BaseFragment
import com.almaz.sarafanka.presentation.main.MainActivity
import com.almaz.sarafanka.presentation.service.ServicesAdapter
import com.almaz.sarafanka.utils.extensions.loadCircleImage
import com.almaz.sarafanka.utils.extensions.observe
import com.almaz.sarafanka.utils.extensions.toGone
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : BaseFragment<ProfileViewModel>(ProfileViewModel::class.java) {
    private val servicesAdapter = ServicesAdapter {
        rootActivity.navController.navigate(
            R.id.action_profileFragment_to_serviceDetailsFragment,
            bundleOf("my_service" to it)
        )
    }
    override val layoutId: Int = R.layout.fragment_profile

    override fun setupView() {
        rv_profile_services.layoutManager = LinearLayoutManager(context)
        rv_profile_services.adapter = servicesAdapter
        viewModel.getProfileInfo()
        viewModel.getProfileServices()
        ib_logout.setOnClickListener {
            viewModel.logout()
            rootActivity.bottom_nav.toGone()
            rootActivity.navController.navigate(R.id.action_profileFragment_to_loginFragment)
        }
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
        servicesAdapter.submitGlobalList(services)
        servicesAdapter.notifyDataSetChanged()
    }
}
