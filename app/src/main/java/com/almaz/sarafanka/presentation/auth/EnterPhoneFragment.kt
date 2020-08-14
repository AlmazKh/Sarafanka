package com.almaz.sarafanka.presentation.auth

import android.telephony.PhoneNumberFormattingTextWatcher
import androidx.lifecycle.ViewModelProvider
import com.almaz.sarafanka.R
import com.almaz.sarafanka.presentation.base.BaseFragment
import com.almaz.sarafanka.utils.AuthState
import com.almaz.sarafanka.utils.extensions.observe
import kotlinx.android.synthetic.main.fragment_enter_phone.*

class EnterPhoneFragment : BaseFragment<AuthViewModel>(AuthViewModel::class.java) {

    override val layoutId: Int = R.layout.fragment_enter_phone

    override fun setViewModel() {
        viewModel = ViewModelProvider(rootActivity, this.viewModelFactory)
            .get(AuthViewModel::class.java)
    }

    override fun setupView() {
        et_phone_number.addTextChangedListener(PhoneNumberFormattingTextWatcher())
        btn_next_to_verification.setOnClickListener {
            checkPhoneNumber("${et_country_code.text}${et_phone_number.text}")
        }
    }

    override fun subscribe(viewModel: AuthViewModel) {
        super.subscribe(viewModel)
        observe(viewModel.authState, ::bindAuthState)
    }

    private fun bindAuthState(authState: AuthState) {
        when (authState) {
            AuthState.CODE_SENDED -> {
                rootActivity.navController.navigate(R.id.action_enterPhoneFragment_to_enterCodeFragment)
            }
            AuthState.ERROR -> {
            }
            else -> {
            }
        }
    }

    private fun checkPhoneNumber(phone: String) {
        if (phone.length < 12) {
            showSnackbar("Введите корректный номер телефона")
        } else {
            viewModel.getVerificationCode(phone)
        }
    }
}
