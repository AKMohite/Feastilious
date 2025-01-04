package com.mak.feastit.domain.legacyusecase

abstract class LegacyUseCaseWithParams<in Params, out R> {
    /**
     * Build the use case to be executed.
     *
     * @param params required to build this use case.
     *
     * @return result [R] of the use case.
     */
    protected abstract fun buildUseCase(params: Params) : R

    /**
     * Execute the use case.
     *
     * Called by client of the use case.
     *
     * @return [R] result of executing this use case
     */
    fun execute(params: Params): R = buildUseCase(params)
}