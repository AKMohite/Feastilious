package com.ak.feastit.base

import androidx.lifecycle.ViewModel
import com.mak.feastit.domain.util.DispatcherProvider
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

internal open class BaseViewModel(
    private val dispatcher: DispatcherProvider
): ViewModel() {

    /**
     * This is the job for all coroutines started by this ViewModel.
     * Cancelling this job will cancel all coroutines started by this ViewModel.
     */
    private val superVisorJob = SupervisorJob()

    /**
     * Handle exception to display a message instead of crashing
     */
    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        handleError(exception)
    }

    /**
     * This is the main scope for all coroutines launched by this ViewModel.
     * Since we pass [superVisorJob], you can cancel all coroutines
     * launched by viewModelScope by calling [viewModelJob.cancel()]
     */
    val uiScope
        get() = CoroutineScope(dispatcher.main + superVisorJob + exceptionHandler)

    protected open fun handleError(exception: Throwable) {

    }

}