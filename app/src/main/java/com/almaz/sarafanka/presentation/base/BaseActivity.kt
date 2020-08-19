package com.almaz.sarafanka.presentation.base

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.almaz.sarafanka.SarafankaApp
import com.almaz.sarafanka.utils.states.LoadingState
import com.almaz.sarafanka.utils.extensions.observe
import com.almaz.sarafanka.utils.extensions.toGone
import com.almaz.sarafanka.utils.extensions.toVisible
import kotlinx.android.synthetic.main.error_layer.*
import kotlinx.android.synthetic.main.success_layer.*
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
        observe(viewModel.errorState, ::bindErrorState)
        observe(viewModel.successState, ::bindSuccessState)
    }

    private fun bindLoadingState(loadingState: LoadingState) {
        when (loadingState) {
            LoadingState.LOADING -> {
            }
            LoadingState.READY -> {
            }
        }
    }

    private fun bindErrorState(error: String) {
        showMessage(error_layer, tv_error, error)
    }

    private fun bindSuccessState(message: String) {
        showMessage(success_layer, tv_success, message)
    }

    private fun showMessage(view: View?, field: TextView, message: String) {
        when (message) {
            "" -> view?.toGone()
            else -> {
                view?.toVisible()
                field.text = message
            }
        }
    }
}
