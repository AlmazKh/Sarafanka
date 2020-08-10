package com.almaz.sarafanka.presentation.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.almaz.sarafanka.SarafankaApp
import com.almaz.sarafanka.utils.LoadingState
import com.almaz.sarafanka.utils.extensions.observe
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

abstract class BaseActivity<T : BaseViewModel>(private val classT: Class<T>) : AppCompatActivity(),
    KodeinAware {

    protected abstract val layoutId: Int

    protected open val viewModelFactory: ViewModelProvider.Factory by instance()
    val viewModel: T by lazy { viewModelFactory.create(classT) }

    override val kodein: Kodein by lazy { SarafankaApp.app.kodein }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
        setupView()
        subscribe(viewModel)
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
