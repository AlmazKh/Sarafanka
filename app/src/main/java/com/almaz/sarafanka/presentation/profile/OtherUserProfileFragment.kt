package com.almaz.sarafanka.presentation.profile

import android.content.Intent
import android.net.Uri
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.almaz.sarafanka.R
import com.almaz.sarafanka.core.model.Service
import com.almaz.sarafanka.core.model.User
import com.almaz.sarafanka.presentation.base.BaseFragment
import com.almaz.sarafanka.presentation.service.ServicesAdapter
import com.almaz.sarafanka.utils.extensions.loadCircleImage
import com.almaz.sarafanka.utils.extensions.observe
import kotlinx.android.synthetic.main.fragment_other_user_profile.*
import kotlinx.android.synthetic.main.fragment_profile.iv_user_avatar
import kotlinx.android.synthetic.main.fragment_profile.rv_profile_services
import kotlinx.android.synthetic.main.fragment_profile.tv_user_name

class OtherUserProfileFragment : BaseFragment<ProfileViewModel>(ProfileViewModel::class.java) {
    private val servicesAdapter = ServicesAdapter {
        rootActivity.navController.navigate(
            R.id.action_otherProfileFragment_to_serviceDetailsFragment,
            bundleOf("other_service" to it)
        )
    }
    override val layoutId: Int = R.layout.fragment_other_user_profile

    override fun setupView() {
        rv_profile_services.layoutManager = LinearLayoutManager(context)
        rv_profile_services.adapter = servicesAdapter
        arguments?.get("profile").toString().run {
            viewModel.getProfileInfo(this)
            viewModel.getProfileServices(this)
        }
        btn_back.setOnClickListener {
            rootActivity.navController.navigateUp()
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
        btn_contact.setOnClickListener {
            startActivity(Intent(Intent.ACTION_DIAL).apply {
                data = Uri.fromParts("tel", user.phone, null)
            })
        }
    }

    private fun bindProfileServices(services: List<Service>) {
        servicesAdapter.submitGlobalList(services)
        servicesAdapter.notifyDataSetChanged()
    }
}