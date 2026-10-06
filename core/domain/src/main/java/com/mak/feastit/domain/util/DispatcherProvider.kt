// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.mak.feastit.domain.util

import kotlinx.coroutines.CoroutineDispatcher

interface DispatcherProvider {
  val io: CoroutineDispatcher
  val computation: CoroutineDispatcher
  val main: CoroutineDispatcher
}
