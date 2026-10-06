// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.mak.feastit.domain.usecase

abstract class ObserveUsecase<Params, Output> {
  abstract operator fun invoke(params: Params): Output
}
