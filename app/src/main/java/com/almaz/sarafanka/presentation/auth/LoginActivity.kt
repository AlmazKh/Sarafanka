package com.almaz.sarafanka.presentation.auth

import com.almaz.sarafanka.R
import com.almaz.sarafanka.presentation.base.BaseActivity

class LoginActivity : BaseActivity<LoginViewModel>(LoginViewModel::class.java) {
    override val layoutId: Int
        get() = R.layout.activity_login

    override fun setupView() {
    }

}
