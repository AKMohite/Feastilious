package com.mak.feastit.domain.repository

interface StaleRepository {
    suspend fun removeStaleData()
}