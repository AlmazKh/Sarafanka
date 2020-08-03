package com.almaz.sarafanka.presentation.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.almaz.sarafanka.utils.LoadingState
import com.almaz.sarafanka.utils.extensions.observe
import org.kodein.di.KodeinAware

abstract class BaseActivity<T : BaseViewModel>(val classT: Class<T>) : AppCompatActivity(),
    KodeinAware {

    protected abstract val layoutId: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
        setupView()
    }

    protected abstract fun setupView()

    open fun subscribe(viewModel: T) {
        observe(viewModel.loadingState, ::bindLoadingState)
    }

    private fun bindLoadingState(loadingState: LoadingState) {
        when (loadingState) {
            LoadingState.LOADING -> {
            }
            LoadingState.READY -> {
            }
        }
    }
}
