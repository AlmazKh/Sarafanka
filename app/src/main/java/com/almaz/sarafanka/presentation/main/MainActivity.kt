package com.almaz.sarafanka.presentation.main

import android.os.Build
import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.almaz.sarafanka.R
import com.almaz.sarafanka.presentation.base.BaseActivity
import com.almaz.sarafanka.utils.extensions.observe
import com.almaz.sarafanka.utils.extensions.toGone
import com.almaz.sarafanka.utils.extensions.toVisible
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity<MainViewModel>(MainViewModel::class.java) {

    override val layoutId: Int = R.layout.activity_main

    val navController by lazy { findNavController(R.id.nav_host_fragment) }

    override fun setupView() {
        setUpStatusBar()
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

    private fun setUpStatusBar() {
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorWhite)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    private fun navigateByAuthState(isLoggedIn: Boolean) {
        when (isLoggedIn) {
            true -> {
                bottom_nav.toVisible()
            }
            else -> {
                bottom_nav.toGone()
                val navOptions: NavOptions =
                    NavOptions.Builder()
                        .setLaunchSingleTop(true)
                        .setPopUpTo(R.id.auth_nav_graph, true)
                        .build()
                navController.navigate(R.id.auth_nav_graph, null, navOptions)
            }
        }
    }
}
