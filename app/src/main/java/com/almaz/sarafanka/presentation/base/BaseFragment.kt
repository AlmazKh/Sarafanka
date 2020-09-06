package com.almaz.sarafanka.presentation.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.almaz.sarafanka.SarafankaApp
import com.almaz.sarafanka.presentation.main.MainActivity
import com.almaz.sarafanka.utils.states.LoadingState
import com.almaz.sarafanka.utils.extensions.observe
import com.google.android.material.snackbar.Snackbar
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

abstract class BaseFragment<T : BaseViewModel>(private val classT: Class<T>) : Fragment(),
    KodeinAware {
    protected lateinit var rootActivity: MainActivity
    protected abstract val layoutId: Int

    val viewModelFactory: ViewModelProvider.Factory by instance()
    lateinit var viewModel: T

    override val kodein: Kodein by lazy { (rootActivity.application as SarafankaApp).kodein }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        rootActivity = activity as MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setViewModel()
        subscribe(viewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        subscribe(viewModel)
    }

    open fun subscribe(viewModel: T) {
        observe(viewModel.loadingState, ::bindLoadingState)
    }

    protected abstract fun setupView()

    open fun setViewModel() {
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

    fun showSnackbar(message: String) {
        view?.let { it1 ->
            Snackbar.make(
                it1,
                message,
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }
}
