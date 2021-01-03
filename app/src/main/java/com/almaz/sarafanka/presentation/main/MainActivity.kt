package com.almaz.sarafanka.presentation.main

import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.almaz.sarafanka.R
import com.almaz.sarafanka.presentation.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity<MainViewModel>(MainViewModel::class.java) {

    override val layoutId: Int = R.layout.activity_main

    val navController by lazy { findNavController(R.id.nav_host_fragment) }

    override fun setupView() {
        setUpStatusBar()
        bottom_nav?.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

    private fun setUpStatusBar() {
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorWhite)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }
}
