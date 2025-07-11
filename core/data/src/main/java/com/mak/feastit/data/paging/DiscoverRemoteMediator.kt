// Copyright 2025, Ashish Mohite and the Yum Byte project contributors
// License Name: <Actual name>
package com.mak.feastit.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.mak.feastit.database.entity.custom.PaginatedRecipeEntity
import kotlinx.coroutines.CancellationException
import timber.log.Timber

@OptIn(ExperimentalPagingApi::class)
internal class DiscoverRemoteMediator(
  private val fetch: suspend (page: Int) -> Unit,
  private val isCacheValid: suspend () -> Boolean,
) : RemoteMediator<Int, PaginatedRecipeEntity>() {
  override suspend fun load(
    loadType: LoadType,
    state: PagingState<Int, PaginatedRecipeEntity>,
  ): MediatorResult {
    return try {
      // The network load method takes an optional after=<user.id>
      // parameter. For every page after the first, pass the last user
      // ID to let it continue from where it left off. For REFRESH,
      // pass null to load the first page.
      val loadKey =
        when (loadType) {
          LoadType.REFRESH -> 1
          // In this example, you never need to prepend, since REFRESH
          // will always load the first page in the list. Immediately
          // return, reporting end of pagination.
          LoadType.PREPEND ->
            return MediatorResult.Success(endOfPaginationReached = true)

          LoadType.APPEND -> {
            val lastItem =
              state.lastItemOrNull()
                ?: return MediatorResult.Success(
                  endOfPaginationReached = true,
                )

            // You must explicitly check if the last item is null when
            // appending, since passing null to networkService is only
            // valid for initial load. If lastItem is null it means no
            // items were loaded after the initial REFRESH and there are
            // no more items to load.

            lastItem.page + 1
          }
        }

      fetch(loadKey)

//            TODO handle is last fetched?
      MediatorResult.Success(
        endOfPaginationReached = false,
      )
    } catch (ce: CancellationException) {
      Timber.e(ce)
      throw ce
    } catch (t: Throwable) {
      Timber.e(t)
      MediatorResult.Error(t)
    }
  }

  override suspend fun initialize(): InitializeAction = if (isCacheValid()) {
    // Cached data is up-to-date, so there is no need to re-fetch
    // from the network.
    InitializeAction.SKIP_INITIAL_REFRESH
  } else {
    // Need to refresh cached data from network; returning
    // LAUNCH_INITIAL_REFRESH here will also block RemoteMediator's
    // APPEND and PREPEND from running until REFRESH succeeds.
    InitializeAction.LAUNCH_INITIAL_REFRESH
  }
}
