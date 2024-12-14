package com.mak.feastit.domain.model

data class PaginatedParams(
    val page: Int = 1,
    val forceRefresh: Boolean = false
)
