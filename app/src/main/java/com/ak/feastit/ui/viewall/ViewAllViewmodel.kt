package com.ak.feastit.ui.viewall

import androidx.lifecycle.SavedStateHandle
import com.ak.feastit.base.BaseViewModel
import com.mak.feastit.domain.util.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class ViewAllViewmodel @Inject constructor(
    private val dispatcher: DispatcherProvider,
    private val savedState: SavedStateHandle
): BaseViewModel(dispatcher) {

    init {
        Timber.d("${savedState.get<String?>("category")}")
        Timber.d("${savedState.get<String?>("category_sub_type")}")
    }

}