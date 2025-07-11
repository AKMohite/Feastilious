// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.utils

import com.mak.feastit.domain.util.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

internal class AppDispatcher : DispatcherProvider {
  override val io: CoroutineDispatcher
    get() = Dispatchers.IO
  override val computation: CoroutineDispatcher
    get() = Dispatchers.Default
  override val main: CoroutineDispatcher
    get() = Dispatchers.Main
}
