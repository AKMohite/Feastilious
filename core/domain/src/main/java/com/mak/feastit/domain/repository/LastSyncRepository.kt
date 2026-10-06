// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.mak.feastit.domain.repository

import com.mak.feastit.domain.model.SyncRequest
import com.mak.feastit.domain.model.SyncType

interface LastSyncRepository {
//    TODO maybe pass boolean if need to sync or not
  suspend fun getLastSyncFor(request: SyncType): SyncRequest

  suspend fun needSync(request: SyncType): Boolean

  suspend fun updateLastSync(request: SyncType)
}
