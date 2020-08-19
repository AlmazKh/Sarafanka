package com.almaz.sarafanka.presentation.profile

import com.almaz.sarafanka.R
import com.almaz.sarafanka.core.model.User
import com.almaz.sarafanka.presentation.base.BaseFragment
import com.almaz.sarafanka.utils.extensions.loadCircleImage
import com.almaz.sarafanka.utils.extensions.observe
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : BaseFragment<ProfileViewModel>(ProfileViewModel::class.java) {
    override val layoutId: Int = R.layout.fragment_profile

    override fun setupView() {

    }

    override fun subscribe(viewModel: ProfileViewModel) {
        super.subscribe(viewModel)
        observe(viewModel.profileInfoLiveData, ::bindProfileInfo)
    }

    private fun bindProfileInfo(user: User) {
        tv_user_name.text = user.name
        user.photo?.let { iv_user_avatar.loadCircleImage(it) }
    }
}
