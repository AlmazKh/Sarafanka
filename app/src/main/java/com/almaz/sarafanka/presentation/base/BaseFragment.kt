package com.almaz.sarafanka.presentation.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.almaz.sarafanka.SarafankaApp
import com.almaz.sarafanka.presentation.main.MainActivity
import com.almaz.sarafanka.utils.extensions.observe
import com.almaz.sarafanka.utils.extensions.toGoneAnimated
import com.almaz.sarafanka.utils.extensions.toVisibleAnimated
import com.almaz.sarafanka.utils.states.LoadingState
import com.faltenreich.skeletonlayout.Skeleton
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.android.synthetic.main.fragment_service.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

abstract class BaseFragment<T : BaseViewModel>(private val classT: Class<T>) : Fragment(),
    KodeinAware, SwipeRefreshLayout.OnRefreshListener {
    protected lateinit var rootActivity: MainActivity
    protected abstract val layoutId: Int
    val skeletons = arrayListOf<Skeleton>()

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
        subscribe(viewModel)
        swipe_refresh?.setOnRefreshListener(this)
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
                skeletons.forEach {
                    it.showSkeleton()
                }
            }
            LoadingState.LOADING_FULLSCREEN -> {
                loading_fullscreen_view?.toVisibleAnimated()
            }
            LoadingState.READY -> {
                swipe_refresh?.isRefreshing = false
                skeletons.forEach {
                    it.showOriginal()
                }
                loading_fullscreen_view?.toGoneAnimated()
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

    override fun onRefresh() {
        swipe_refresh?.isRefreshing = true
        viewModel.refresh()
    }
}
