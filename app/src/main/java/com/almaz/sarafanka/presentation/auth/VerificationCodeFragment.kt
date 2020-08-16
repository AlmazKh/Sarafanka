package com.almaz.sarafanka.presentation.auth

import androidx.lifecycle.ViewModelProvider
import com.almaz.sarafanka.R
import com.almaz.sarafanka.presentation.base.BaseFragment
import com.almaz.sarafanka.utils.AuthState
import com.almaz.sarafanka.utils.extensions.observe
import kotlinx.android.synthetic.main.fragment_verification_code.*

class VerificationCodeFragment : BaseFragment<AuthViewModel>(AuthViewModel::class.java) {

    override val layoutId: Int = R.layout.fragment_verification_code

    override fun setViewModel() {
        viewModel = ViewModelProvider(rootActivity, this.viewModelFactory)
            .get(AuthViewModel::class.java)
    }

    override fun setupView() {
        btn_approve_verification.setOnClickListener {
            if (et_verification_code.text.isNullOrBlank()) {
                showSnackbar("Введите код")
            } else {
                viewModel.verifySignInCode(et_verification_code.text.toString())
            }
        }
    }

    override fun subscribe(viewModel: AuthViewModel) {
        super.subscribe(viewModel)
        observe(viewModel.authState, ::bindAuthState)
    }

    private fun bindAuthState(authState: AuthState) {
        when (authState) {
            AuthState.LOGGEDIN -> {
                rootActivity.navController.navigate(R.id.action_enterCodeFragment_to_enterNameFragment)
            }
            AuthState.REGISTERED -> {
                rootActivity.navController.navigate(R.id.action_enterCodeFragment_to_nav_graph)
            }
        }
    }
}
