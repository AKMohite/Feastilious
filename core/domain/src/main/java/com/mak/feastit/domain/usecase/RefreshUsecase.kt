// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.mak.feastit.domain.usecase

internal abstract class RefreshUsecase<Params, Output> {
  abstract suspend operator fun invoke(params: Params): Output
}
