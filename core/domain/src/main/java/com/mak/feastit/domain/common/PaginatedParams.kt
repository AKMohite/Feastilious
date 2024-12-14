package com.mak.feastit.domain.common

data class PaginatedParams(
    val page: Int = 1,
    val forceRefresh: Boolean = false
)
