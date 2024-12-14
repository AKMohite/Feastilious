package com.mak.feastit.domain.common

interface RefreshableContent<Param, Output> {
    suspend operator fun invoke(params: Param): Output
}