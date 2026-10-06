// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.mak.feastit.remote

import okhttp3.Interceptor
import okhttp3.Response

class ErrorInterceptor(
  private val key: String,
) : Interceptor {
  override fun intercept(chain: Interceptor.Chain): Response {
    val original = chain.request()
    val url =
      original.url
        .newBuilder()
        .addQueryParameter("apiKey", key)
        .build()
    val request = original.newBuilder().url(url)
    return chain.proceed(request.build())
  }
}
