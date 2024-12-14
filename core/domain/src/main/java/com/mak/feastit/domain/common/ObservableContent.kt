package com.mak.feastit.domain.common

import kotlinx.coroutines.flow.Flow

internal interface ObservableContent<Param, Output> {
    operator fun invoke(params: Param): Flow<Output>
}