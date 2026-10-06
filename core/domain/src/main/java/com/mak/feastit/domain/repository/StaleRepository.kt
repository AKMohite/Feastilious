// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.mak.feastit.domain.repository

interface StaleRepository {
  suspend fun removeStaleData()
}
