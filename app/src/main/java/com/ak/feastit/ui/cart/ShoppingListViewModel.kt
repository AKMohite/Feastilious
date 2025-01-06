package com.ak.feastit.ui.cart

import androidx.lifecycle.ViewModel
import com.ak.feastit.base.BaseViewModel
import com.mak.feastit.domain.util.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class ShoppingListViewModel @Inject constructor(
    dispatcher: DispatcherProvider
) : BaseViewModel(dispatcher) {

}