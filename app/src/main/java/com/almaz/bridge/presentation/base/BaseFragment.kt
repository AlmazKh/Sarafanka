package com.almaz.bridge.presentation.base

import android.content.Context
import androidx.fragment.app.Fragment
import com.almaz.bridge.BridgeApp
import com.almaz.bridge.presentation.main.MainActivity
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware


open class BaseFragment : Fragment(), KodeinAware {
    lateinit var rootActivity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        rootActivity = activity as MainActivity
    }

    override val kodein: Kodein by lazy {
        (rootActivity.application as BridgeApp).kodein
    }
}
