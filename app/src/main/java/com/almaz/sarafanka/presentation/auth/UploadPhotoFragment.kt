package com.almaz.sarafanka.presentation.auth

import androidx.lifecycle.ViewModelProvider
import com.almaz.sarafanka.R
import com.almaz.sarafanka.presentation.base.BaseFragment
import com.almaz.sarafanka.utils.AuthState
import com.almaz.sarafanka.utils.extensions.observe
import kotlinx.android.synthetic.main.fragment_upload_photo.*

class UploadPhotoFragment : BaseFragment<AuthViewModel>(AuthViewModel::class.java) {

    override val layoutId: Int = R.layout.fragment_upload_photo

    override fun setViewModel() {
        viewModel = ViewModelProvider(rootActivity, this.viewModelFactory)
            .get(AuthViewModel::class.java)
    }

    override fun setupView() {
        btn_approve_auth.setOnClickListener {
            viewModel.authState.postValue(AuthState.REGISTERED)
        }
    }

    override fun subscribe(viewModel: AuthViewModel) {
        super.subscribe(viewModel)
        observe(viewModel.authState, ::bindAuthState)
    }

    private fun bindAuthState(authState: AuthState) {
        when (authState) {
            AuthState.REGISTERED -> {
                rootActivity.navController.navigate(R.id.action_enterPhotoFragment_to_nav_graph)
            }
        }
    }
}
