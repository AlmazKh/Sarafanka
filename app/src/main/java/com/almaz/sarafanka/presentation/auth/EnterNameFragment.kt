package com.almaz.sarafanka.presentation.auth

import androidx.lifecycle.ViewModelProvider
import com.almaz.sarafanka.R
import com.almaz.sarafanka.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_enter_name.*

class EnterNameFragment : BaseFragment<AuthViewModel>(AuthViewModel::class.java) {

    override val layoutId: Int = R.layout.fragment_enter_name

    override fun setViewModel() {
        viewModel = ViewModelProvider(rootActivity, this.viewModelFactory)
            .get(AuthViewModel::class.java)
    }

    override fun setupView() {
        btn_next_to_upload_photo.setOnClickListener {
            rootActivity.navController.navigate(R.id.action_enterNameFragment_to_enterPhotoFragment)
        }
    }
}
