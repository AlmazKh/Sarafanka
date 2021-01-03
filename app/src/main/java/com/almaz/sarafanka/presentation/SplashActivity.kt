package com.almaz.sarafanka.presentation

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.almaz.sarafanka.R
import com.almaz.sarafanka.data.AuthManager
import com.almaz.sarafanka.presentation.auth.AuthActivity
import com.almaz.sarafanka.presentation.main.MainActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var open = { openAuth() }
        Log.i("Splash", AuthManager(this).isLoggedIn().toString())
        if (AuthManager(this).isLoggedIn())
            open = { openMain() }
        Handler().postDelayed({
            open()
        }, 1000)
    }

    private fun openAuth() {
        startActivity(Intent(this, AuthActivity::class.java))
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        finish()
    }

    private fun openMain() {
        startActivity(Intent(this, MainActivity::class.java))
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        finish()
    }
}
