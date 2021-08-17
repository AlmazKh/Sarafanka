package com.almaz.sarafanka.core.interactor

import androidx.lifecycle.MutableLiveData
import com.almaz.sarafanka.utils.states.LoadingState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

abstract class BaseInteractor : CoroutineScope {
    val loadingState = MutableLiveData(LoadingState.READY)

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext = job + Dispatchers.Main
}