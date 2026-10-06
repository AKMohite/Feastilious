// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.ak.feastit.core.common

import coil3.intercept.Interceptor
import coil3.request.ImageResult
import com.ak.feastit.core.support.PowerController
import com.mak.feastit.domain.model.RecipeImage
import javax.inject.Inject

internal class RecipeImageInterceptor @Inject constructor(
  private val powerController: PowerController,
) : Interceptor {

  override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
    return when (val data = chain.request.data) {
      is RecipeImage -> createNewChain(chain, data).proceed()
      else -> chain.proceed()
    }
  }

  private suspend fun createNewChain(
    chain: Interceptor.Chain,
    image: RecipeImage,
  ): Interceptor.Chain {
    if (powerController.needToSaveData()) {
      image.setSaver()
    }
    val newRequest = chain.request.newBuilder()
      .data(image.getResource())
      .build()
    return chain.withRequest(newRequest)
  }
}
