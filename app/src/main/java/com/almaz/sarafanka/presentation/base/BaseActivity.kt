package com.almaz.sarafanka.presentation.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.almaz.sarafanka.utils.LoadingState
import com.almaz.sarafanka.utils.extensions.observe
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

abstract class BaseActivity<T : BaseViewModel>(val classT: Class<T>) : AppCompatActivity(),
    KodeinAware {

    protected abstract val layoutId: Int

    private val viewModelFactory: ViewModelProvider.Factory by instance()
    lateinit var viewModel: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
        setupView()
        setViewModel()
        subscribe(viewModel)
    }

    protected abstract fun setupView()

    open fun subscribe(viewModel: T) {
        observe(viewModel.loadingState, ::bindLoadingState)
    }

    private fun setViewModel() {
        viewModel = viewModelFactory.create(classT)
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
