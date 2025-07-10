package com.mak.feastit.domain.usecase

internal abstract class RefreshUsecase<Params, Output> {
    abstract suspend operator fun invoke(params: Params): Output
}