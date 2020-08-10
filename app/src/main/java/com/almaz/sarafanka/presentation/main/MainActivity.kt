package com.almaz.sarafanka.presentation.main

import android.content.Intent
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.almaz.sarafanka.R
import com.almaz.sarafanka.presentation.auth.LoginActivity
import com.almaz.sarafanka.presentation.base.BaseActivity
import com.almaz.sarafanka.utils.extensions.observe
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<MainViewModel>(MainViewModel::class.java) {

    override val layoutId: Int = R.layout.activity_main

    val navController by lazy { findNavController(R.id.nav_host_fragment) }

    override fun setupView() {
        viewModel.checkAuthUser()
        bottom_nav?.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

    override fun subscribe(viewModel: MainViewModel) {
        super.subscribe(viewModel)
        observe(viewModel.isLoggedIn, ::navigateByAuthState)
    }

    private fun navigateByAuthState(isLoggedIn: Boolean) {
        when (isLoggedIn) {
            true -> {
            }
            else -> {
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }
    }
}
