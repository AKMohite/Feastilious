// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.core.common

import coil3.intercept.Interceptor
import coil3.request.ImageResult
import com.mak.feastit.domain.model.RecipeImage
import com.mak.feastit.domain.util.DispatcherProvider
import javax.inject.Inject

internal class RecipeImageInterceptor @Inject constructor(
  private val dispatcherProvider: DispatcherProvider,
) : Interceptor {

  // TODO get from settings preference
  private val isDataSaverEnabled: Boolean = true

  override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
    return when (val data = chain.request.data) {
      is RecipeImage -> createNewRequest(chain, data).proceed()
      else -> chain.proceed()
    }
  }

  private fun createNewRequest(
    chain: Interceptor.Chain,
    image: RecipeImage,
  ): Interceptor.Chain {
    if (isDataSaverEnabled) {
      image.setSaver()
    }
    val newRequest = chain.request.newBuilder()
      .data(image.getResource())
      .build()
    return chain.withRequest(newRequest)
  }
}
