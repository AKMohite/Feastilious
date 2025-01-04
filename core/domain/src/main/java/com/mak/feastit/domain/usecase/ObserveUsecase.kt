package com.mak.feastit.domain.usecase

abstract class ObserveUsecase<Params, Output> {
    abstract operator fun invoke(params: Params): Output
}